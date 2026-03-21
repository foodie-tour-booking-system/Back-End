package org.foodie_tour.modules.booking.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.exception.GlobalExceptionHandler;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.utils.MailSampleText;
import org.foodie_tour.common.utils.RandomCode;
import org.foodie_tour.modules.auth.entity.OtpCode;
import org.foodie_tour.modules.auth.enums.TokenScope;
import org.foodie_tour.modules.auth.repository.OtpCodeRepository;
import org.foodie_tour.modules.auth.service.AuthService;
import org.foodie_tour.modules.booking.dto.request.BookingCancelRequest;
import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.request.ProcessRelocateRequest;
import org.foodie_tour.modules.booking.dto.request.RelocateBookingRequest;
import org.foodie_tour.modules.booking.dto.request.RescheduleRequest;
import org.foodie_tour.modules.booking.dto.response.BookingLogResponse;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;
import org.foodie_tour.modules.booking.dto.response.RelocateBookingResponse;
import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.entity.BookingLog;
import org.foodie_tour.modules.booking.entity.RelocateBooking;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.booking.enums.PaymentMethod;
import org.foodie_tour.modules.booking.enums.RefundStatus;
import org.foodie_tour.modules.booking.enums.RelocateRequestStatus;
import org.foodie_tour.modules.booking.mapper.BookingLogMapper;
import org.foodie_tour.modules.booking.mapper.BookingMapper;
import org.foodie_tour.modules.booking.mapper.RelocateBookingMapper;
import org.foodie_tour.modules.booking.repository.BookingLogRepository;
import org.foodie_tour.modules.booking.repository.BookingRepository;
import org.foodie_tour.modules.booking.repository.RelocateBookingRepository;
import org.foodie_tour.modules.booking.service.BookingService;
import org.foodie_tour.modules.customer.entity.Customer;
import org.foodie_tour.modules.customer.entity.CustomerBooking;
import org.foodie_tour.modules.customer.enums.CustomerStatus;
import org.foodie_tour.modules.customer.repository.CustomerBookingRepository;
import org.foodie_tour.modules.customer.repository.CustomerRepository;
import org.foodie_tour.modules.onepay.service.OnePayService;
import org.foodie_tour.modules.mail.dto.request.SendMailRequest;
import org.foodie_tour.modules.mail.service.MailService;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.system.entity.SystemConfig;
import org.foodie_tour.modules.system.repository.SystemConfigRepository;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.transaction.entity.Transactions;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.foodie_tour.modules.transaction.enums.TransactionStatus;
import org.foodie_tour.modules.transaction.repository.TransactionsRepository;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;
import org.foodie_tour.modules.vnpay.service.VNPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;
    BookingLogRepository bookingLogRepository;
    ScheduleRepository scheduleRepository;
    BookingMapper bookingMapper;
    BookingLogMapper bookingLogMapper;
    VNPayService vnPayService;
    CustomerRepository customerRepository;
    CustomerBookingRepository customerBookingRepository;
    OnePayService onePayService;
    MailService mailService;
    OtpCodeRepository otpCodeRepository;
    AuthService authService;
    RelocateBookingRepository relocateBookingRepository;
    RelocateBookingMapper relocateBookingMapper;
    private final SystemConfigRepository systemConfigRepository;
    private final TransactionsRepository transactionsRepository;

    @Override
    public String rescheduleBooking(RescheduleRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking với ID: " + request.getBookingId()));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED || booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new InvalidateDataException("Không thể dời tour với trạng thái " + booking.getBookingStatus());
        }

        Schedule oldSchedule = booking.getSchedule();

        // Tìm lịch trình mới: ưu tiên dùng scheduleId nếu có
        Schedule newSchedule;
        if (request.getScheduleId() != null) {
            newSchedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch trình với ID: " + request.getScheduleId()));
        } else {
            Tour tour = oldSchedule.getTour();
            newSchedule = scheduleRepository.findByTourAndDepartureAt(tour, request.getNewTourDate())
                    .orElseThrow(() -> new ResourceNotFoundException("Không có lịch trình cho tour này vào ngày được chọn."));
        }

        if (newSchedule.getScheduleStatus() == ScheduleStatus.DELETED
                || newSchedule.getScheduleStatus() == ScheduleStatus.INACTIVE) {
            throw new InvalidateDataException("Lịch trình mới không khả dụng.");
        }

        // Update booking with new schedule
        booking.setSchedule(newSchedule);
        booking.setBookingStatus(BookingStatus.RESCHEDULED);
        bookingRepository.save(booking);

        // Create a log for the reschedule action
        BookingLog bookingLog = BookingLog.builder()
                .booking(booking)
                .bookingStatus(BookingStatus.RESCHEDULED)
                .description("Tour đã được dời từ " + oldSchedule.getDepartureAt() + " sang " + newSchedule.getDepartureAt() + ". Lý do: " + request.getReason())
                .build();
        bookingLogRepository.save(bookingLog);

        return "Dời tour thành công. Lịch trình mới của bạn là vào " + newSchedule.getDepartureAt();
    }



    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .map(existing -> {
                    existing.setCustomerName(request.getCustomerName());
                    existing.setPhone(request.getPhone());
                    existing.setStatus(CustomerStatus.PENDING);
                    return customerRepository.save(existing);
                })
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(request.getEmail());
                    newCustomer.setCustomerName(request.getCustomerName());
                    newCustomer.setPhone(request.getPhone());
                    newCustomer.setStatus(CustomerStatus.PENDING);
                    return customerRepository.save(newCustomer);
                });

        Schedule template = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Khung giờ khởi hành không tồn tại"));

        LocalTime startTime = template.getTime() != null 
                ? template.getTime() 
                : template.getDepartureAt().toLocalTime();

        LocalDateTime actualDepartureAt = LocalDateTime.of(
                request.getDepartureDate(),
                startTime
        );

        Tour tour = template.getTour();
        if (tour == null) throw new ResourceNotFoundException("Tour không tồn tại");

        Booking booking = bookingMapper.toBooking(request);

        booking.setSchedule(template);
        booking.setTour(tour);
        booking.setRoute(template.getRoute());
        booking.setDepartureTime(actualDepartureAt);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setRefundStatus(RefundStatus.INACTIVE);
        booking.setIsDeposit(request.isDeposit());

        booking.setTourType(request.getTourType());

        long adultPrice;
        long childPrice;
        if (request.getTourType() == org.foodie_tour.modules.tours.enums.TourType.PRIVATE) {
            adultPrice = tour.getPrivatePriceAdult();
            childPrice = tour.getPrivatePriceChild();
        } else {
            adultPrice = tour.getGroupPriceAdult();
            childPrice = tour.getGroupPriceChild();
        }
        long totalPrice = (adultPrice * request.getAdultCount()) + (childPrice * request.getChildrenCount());
        booking.setTotalPrice(totalPrice);

        if (request.isDeposit()) {
            long depositAmount = (long) (totalPrice * 0.3); // 30% cọc
            booking.setAmountPaid(0L);
            booking.setRemainingAmount(totalPrice - depositAmount);
        } else {
            booking.setAmountPaid(0L);
            booking.setRemainingAmount(0L);
        }

        if (booking.getBookingCode() == null) {
            String bookingCode = RandomCode.generateRandomCode(10);
            booking.setBookingCode(bookingCode);
        }

        BookingLog log = BookingLog.builder()
                .booking(booking)
                .description("Đặt tour thành công. Khởi hành: " + actualDepartureAt)
                .bookingStatus(booking.getBookingStatus())
                .build();

        if (booking.getBookingLogs() == null) booking.setBookingLogs(new ArrayList<>());
        booking.getBookingLogs().add(log);

        bookingRepository.save(booking);

        CustomerBooking customerBooking = customerBookingRepository.findByBooking(booking)
                .orElseGet(() -> {
                    CustomerBooking newLink = new CustomerBooking();
                    newLink.setBooking(booking);
                    return newLink;
                });
        customerBooking.setCustomer(customer);
        customerBooking.setIsMain(true);
        customerBookingRepository.save(customerBooking);

        int totalCustomer = booking.getAdultCount() + booking.getChildrenCount();
        String emailContent = String.format(MailSampleText.CREATE_BOOKING_CONTENT,
                booking.getBookingCode(),
                customer.getCustomerName(),
                tour.getTourName(),
                booking.getDepartureTime(),
                totalCustomer,
                booking.getTotalPrice());

        mailService.sendMail(new SendMailRequest(
                new String[]{request.getEmail()},
                MailSampleText.CREATE_BOOKING_TITLE,
                emailContent
        ));

        return bookingMapper.toResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingByCode(String bookingCode) {
        return bookingMapper.toResponse(findByBookingCode(bookingCode));
    }

    @Transactional(readOnly = true)
    public List<BookingLogResponse> getLogsByBookingCode(String bookingCode) {
        return bookingLogRepository.getLogsByBookingCode(bookingCode).stream().map(bookingLogMapper::toResponse)
                .toList();
    }

    public String generatePaymentUrl(long bookingId, HttpServletRequest servletRequest) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        PaymentRequest request = new PaymentRequest(bookingId, booking.getTotalPrice());
        if (booking.getPaymentMethod() == PaymentMethod.VNPAY) {
            return vnPayService.generatePaymentUrl(request, servletRequest);
        } else if (booking.getPaymentMethod() == PaymentMethod.VISA) {
            return onePayService.generatePaymentUrl(bookingId, servletRequest);
        }

        throw new InvalidateDataException("Phương thức thanh toán không được hỗ trợ");
    }

    private Booking findByBookingCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));
    }

    @Transactional
    public String requestRelocateBooking(String bookingCode) {
        Booking booking = findByBookingCode(bookingCode);

        // Verify request
        // Time request
        int timeAllow = 8;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = booking.getSchedule().getDepartureAt();
        if (!now.plusHours(timeAllow).isBefore(startTime)) {
            String error = String.format("Chỉ được phép dời lịch trình trước khi khởi hành ít nhất %s giờ", timeAllow);
            throw new InvalidateDataException(error);
        }

        // Generate otp
        String otp;

        do {
            otp = RandomCode.generateRandomCode(6);
        } while (otpCodeRepository.existsById(otp));

        String token = authService.generateTokenByScope(TokenScope.RELOCATE_BOOKING, 15, bookingCode);

        // Save otp
        OtpCode otpEntity = new OtpCode();
        otpEntity.setOtpCode(otp);
        otpEntity.setToken(token);
        otpEntity.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        otpCodeRepository.save(otpEntity);

        // Send code to mail
        String[] receiver = new String[1];
        receiver[0] = booking.getEmail();
        SendMailRequest request = new SendMailRequest(receiver, "Xác nhận dời lịch cho mã đặt lịch " + bookingCode,
                otp);
        mailService.sendMail(request);

        // Return access token for next step
        return token;
    }

    @Transactional
    public void createRelocateBookingRequest(String token, RelocateBookingRequest request) {
        // Verify otp
        OtpCode otpEntity = otpCodeRepository.findById(request.getOtp())
                .orElseThrow(() -> new ResourceNotFoundException("Mã xác nhận không tồn tại"));

        if (!otpEntity.getToken().equals(token)) {
            throw new InvalidateDataException("Mã xác nhận không đúng");
        }

        String bookingCodeFromToken = authService.getSubjectFromToken(token);
        String bookingCodeFromRequest = request.getBookingCode();

        if (!bookingCodeFromToken.equals(bookingCodeFromRequest)) {
            throw new InvalidateDataException("Mã xác nhận không hợp lệ");
        }

        // Clean up otp
        otpCodeRepository.delete(otpEntity);

        LocalDateTime departureTime = scheduleRepository.getDepartureTime(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Lịch khởi hành không tồn tại"));

        LocalDateTime actual = LocalDateTime.of(request.getDate(), departureTime.toLocalTime());

        // Save request
        RelocateBooking entity = RelocateBooking.builder()
                .bookingCode(request.getBookingCode())
                .scheduleId(request.getScheduleId())
                .departureAt(actual)
                .relocateRequestStatus(RelocateRequestStatus.PENDING)
                .build();

        relocateBookingRepository.save(entity);

        updateBookingStatus(findByBookingCode(request.getBookingCode()), BookingStatus.RESCHEDULED,
                "Đang chờ xử lý yêu cầu dời lịch trình mới");
    }

    @Transactional(readOnly = true)
    public List<RelocateBookingResponse> getAllPendingRelocateRequest() {
        return relocateBookingRepository.getAllByStatus(RelocateRequestStatus.PENDING).stream()
                .map(relocateBookingMapper::toResponse).toList();
    }

    @Transactional
    public BookingResponse processRelocateRequest(ProcessRelocateRequest request) {
        RelocateBooking relocateBooking = relocateBookingRepository.findById(request.getRelocateRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu"));

        String bookingCode = relocateBooking.getBookingCode();

        long scheduleId = relocateBooking.getScheduleId();

        Booking booking = findByBookingCode(bookingCode);

        if (request.isApproved()) {
            if (scheduleRepository.existsById(scheduleId)) {
                Schedule newSchedule = scheduleRepository.getReferenceById(scheduleId);
                LocalDateTime departureTime = relocateBooking.getDepartureAt();
                booking.setSchedule(newSchedule);
                booking.setDepartureTime(departureTime);
            } else {
                throw new ResourceNotFoundException("Lịch khởi hành không tồn tại");
            }

            relocateBooking.setRelocateRequestStatus(RelocateRequestStatus.APPROVED);

            updateBookingStatus(booking, BookingStatus.CONFIRMED, "Yêu cầu dời lịch trình được chấp thuận, booking trở về trạng thái đã xác nhận");
            bookingRepository.save(booking);
        } else {
            relocateBooking.setRelocateRequestStatus(RelocateRequestStatus.REJECTED);
            updateBookingStatus(booking, BookingStatus.CONFIRMED, "Yêu cầu dời lịch trình bị từ chối, booking trở về trạng thái đã xác nhận");
        }

        relocateBookingRepository.save(relocateBooking);

        return bookingMapper.toResponse(booking);
    }

    @Override
    public String cancelBooking(BookingCancelRequest request) {
        Booking booking = bookingRepository.findByBookingCode(request.getBookingCode())
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        if (!booking.getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new InvalidateDataException("Email không khớp với mã đặt tour");
        }

        int allowHours = Integer.parseInt(
                systemConfigRepository.findById("CANCEL_ALLOW_HOURS")
                        .map(SystemConfig::getConfigValue)
                        .orElse("8")
        );

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = booking.getSchedule().getDepartureAt();
        boolean isEligibleForRefund = now.plusHours(allowHours).isBefore(departureTime);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(request.getCancellationReason());

        String refundNote;
        if (isEligibleForRefund) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            if (booking.getTotalPrice() > 0) {
                Transactions refundTrans = Transactions.builder()
                        .booking(booking)
                        .amount(booking.getTotalPrice())
                        .cashFlow(CashFlow.OUTCOME)
                        .status(TransactionStatus.SUCCESS)
                        .paymentMethod(booking.getPaymentMethod())
                        .build();
                transactionsRepository.save(refundTrans);
                booking.setRefundStatus(RefundStatus.COMPLETED);
            }
            refundNote = " Tiền đã được hoàn về ví của bạn. ";
        } else {
            booking.setBookingStatus(BookingStatus.PENDING);
            refundNote = " Tuy nhiên vì sát giờ khởi hành, yêu cầu hoàn tiền của bạn đang chờ bộ phận phê duyệt. ";
        }

        BookingLog log = BookingLog.builder()
                .booking(booking)
                .description("Lý do: " + request.getCancellationReason())
                .bookingStatus(BookingStatus.CANCELLED)
                .build();
        booking.getBookingLogs().add(log);
        bookingRepository.save(booking);

        mailService.sendMail(new SendMailRequest(
                new String[]{booking.getEmail()},
                "XÁC NHẬN HỦY TOUR THÀNH CÔNG - " + booking.getBookingCode(),
                "Chào " + booking.getCustomerName() + ", yêu cầu hủy tour của bạn đã thực hiện thành công."
        ));
        return refundNote;
    }

    @Override
    public String approveManualRefund(String bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking"));

        if (booking.getRefundStatus() == RefundStatus.COMPLETED) {
            throw new InvalidateDataException("Tour này đã được hoàn tiền trước đó.");
        }

        Transactions refundTrans = Transactions.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .cashFlow(CashFlow.OUTCOME)
                .status(TransactionStatus.SUCCESS)
                .paymentMethod(booking.getPaymentMethod())
                .build();
        transactionsRepository.save(refundTrans);
        booking.setRefundStatus(RefundStatus.COMPLETED);

        BookingLog log = BookingLog.builder()
                .booking(booking)
                .description("Admin phê duyệt hoàn tiền thủ công.")
                .bookingStatus(BookingStatus.CANCELLED)
                .build();
        booking.getBookingLogs().add(log);

        bookingRepository.save(booking);

        return "Yêu cầu của bạn đã được phê duyệt.";
    }

    @Override
    public BookingResponse completeOnTourPayment(String bookingCode, PaymentMethod method) {
        return null;
    }

    @Override
    public List<BookingResponse> getAll(BookingStatus bookingStatus,Long scheduleId) {
        Schedule schedule = null;
        if (scheduleId != null) {
            schedule = scheduleRepository.findById(scheduleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lịch trình không tồn tại"));
        }

        return bookingRepository.findByStatusAndSchedule(bookingStatus, schedule).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    private boolean updateBookingStatus(Booking booking, BookingStatus newStatus, String description) {
        if (booking.getBookingStatus() == newStatus) {
            return false;
        }

        booking.setBookingStatus(newStatus);
        BookingLog log = BookingLog.builder()
                .booking(booking)
                .description(description)
                .bookingStatus(newStatus)
                .build();
        booking.getBookingLogs().add(log);
        bookingRepository.save(booking);
        return true;
    }
}

