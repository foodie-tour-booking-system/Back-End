package org.foodie_tour.modules.booking.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.utils.RandomCode;
import org.foodie_tour.modules.auth.entity.OtpCode;
import org.foodie_tour.modules.auth.repository.OtpCodeRepository;
import org.foodie_tour.modules.auth.service.AuthService;
import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.request.ProcessRelocateRequest;
import org.foodie_tour.modules.booking.dto.request.RelocateBookingRequest;
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
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;
import org.foodie_tour.modules.vnpay.service.VNPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final CustomerRepository customerRepository;
    private final CustomerBookingRepository customerBookingRepository;
    OtpCodeRepository otpCodeRepository;
    AuthService authService;
    RelocateBookingRepository relocateBookingRepository;
    RelocateBookingMapper relocateBookingMapper;

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(request.getEmail());
                    newCustomer.setCustomerName(request.getCustomerName());
                    newCustomer.setPhone(request.getPhone());
                    newCustomer.setStatus(CustomerStatus.PENDING);
                    return customerRepository.save(newCustomer);
                });

        //if exist
        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customerRepository.save(customer);

        // Create booking
        Booking booking = bookingMapper.toBooking(request);

        // Default status is pending
        booking.setBookingStatus(BookingStatus.PENDING);

        // Schedule & tour verify
        Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new ResourceNotFoundException("Lịch khởi hành không tồn tại"));
        booking.setSchedule(schedule);

        // Calculate tour price
        Tour tour = schedule.getTour();
        if (tour != null) {
            long adultPrice = tour.getBasePriceAdult() * request.getAdultCount();
            long childPrice = tour.getBasePriceChild() * request.getChildrenCount();

            long total = adultPrice + childPrice;
            booking.setTotalPrice(total);
        } else {
            throw new ResourceNotFoundException("Lịch trình không tồn tại");
        }

        // Booking code
        String bookingCode = RandomCode.generateRandomCode(10);
        booking.setBookingCode(bookingCode);

        // Default refund status is inactive
        booking.setRefundStatus(RefundStatus.INACTIVE);

        // Create log
        BookingLog log = BookingLog.builder()
                .booking(booking)
                .description("Đặt lịch thành công")
                .build();

        booking.getBookingLogs().add(log);

        bookingRepository.save(booking);

        CustomerBooking customerBooking = new CustomerBooking();
        customerBooking.setCustomer(customer);
        customerBooking.setBooking(booking);
        customerBooking.setIsMain(true);
        customerBookingRepository.save(customerBooking);
        return bookingMapper.toResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingByCode(String bookingCode) {
        return bookingMapper.toResponse(findByBookingCode(bookingCode));
    }

    @Transactional(readOnly = true)
    public List<BookingLogResponse> getLogsByBookingCode(String bookingCode) {
        return bookingLogRepository.getLogsByBookingCode(bookingCode).stream().map(bookingLogMapper::toResponse).toList();
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
        return bookingRepository.findByBookingCode(bookingCode).orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));
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

        String token = authService.generateRelocateRequestToken(bookingCode);

        // Save otp
        OtpCode otpEntity = new OtpCode();
        otpEntity.setOtpCode(otp);
        otpEntity.setToken(token);
        otpCodeRepository.save(otpEntity);

        // Send code to mail
        String[] receiver = new String[1];
        receiver[0] = booking.getEmail();
        SendMailRequest request = new SendMailRequest(receiver,"Xác nhận dời lịch cho mã đặt lịch " + bookingCode, otp);
        mailService.sendMail(request);

        // Return access token for next step
        return token;
    }

    @Transactional
    public void createRelocateBookingRequest(String token,  RelocateBookingRequest request) {
        // Verify otp
        OtpCode otpEntity = otpCodeRepository.findById(request.getOtp()).orElseThrow(() -> new ResourceNotFoundException("Mã xác nhận không tồn tại"));

        if (!otpEntity.getToken().equals(token)) {
            throw new InvalidateDataException("Mã xác nhận không đúng");
        }

        String bookingCodeFromToken = authService.getSubjectFromToken(token);
        String bookingCodeFromRequest = request.getBookingCode();

        if (!bookingCodeFromToken.equals(bookingCodeFromRequest)) {
            throw new InvalidateDataException("Mã xác nhận không hợp lệ");
        }

        LocalDateTime departureTime = scheduleRepository.getDepartureTime(request.getScheduleId()).orElseThrow(() -> new ResourceNotFoundException("Lịch khởi hành không tồn tại"));

        // Save request
        RelocateBooking entity = RelocateBooking.builder()
                .bookingCode(request.getBookingCode())
                .scheduleId(request.getScheduleId())
                .departureAt(departureTime)
                .relocateRequestStatus(RelocateRequestStatus.PENDING)
                .build();

        relocateBookingRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<RelocateBookingResponse> getAllPendingRelocateRequest() {
        return relocateBookingRepository.getAllByStatus(RelocateRequestStatus.PENDING).stream().map(relocateBookingMapper::toResponse).toList();
    }

    @Transactional
    public BookingResponse processRelocateRequest(ProcessRelocateRequest request) {
        RelocateBooking relocateBooking = relocateBookingRepository.findById(request.getRelocateRequestId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu"));

        String bookingCode = relocateBooking.getBookingCode();

        long scheduleId = relocateBooking.getScheduleId();

        Booking booking = findByBookingCode(bookingCode);

        if (request.isApproved()) {
            if (scheduleRepository.existsById(scheduleId)) {
                Schedule newSchedule = scheduleRepository.getReferenceById(scheduleId);
                booking.setSchedule(newSchedule);
            } else {
                throw new ResourceNotFoundException("Lịch khởi hành không tồn tại");
            }

            relocateBooking.setRelocateRequestStatus(RelocateRequestStatus.APPROVED);

            bookingRepository.save(booking);
        } else {
            relocateBooking.setRelocateRequestStatus(RelocateRequestStatus.REJECTED);
        }

        relocateBookingRepository.save(relocateBooking);

        return bookingMapper.toResponse(booking);
    }



}