package org.foodie_tour.modules.booking.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.utils.RandomCode;
import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.response.BookingLogResponse;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;
import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.entity.BookingLog;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.booking.enums.RefundStatus;
import org.foodie_tour.modules.booking.mapper.BookingLogMapper;
import org.foodie_tour.modules.booking.mapper.BookingMapper;
import org.foodie_tour.modules.booking.repository.BookingLogRepository;
import org.foodie_tour.modules.booking.repository.BookingRepository;
import org.foodie_tour.modules.booking.service.BookingService;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;
import org.foodie_tour.modules.vnpay.service.VNPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
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

        return bookingMapper.toResponse(booking);
    }

    public BookingResponse getBookingByCode(String bookingCode) {
        return bookingMapper.toResponse(findByBookingCode(bookingCode));
    }

    public List<BookingLogResponse> getLogsByBookingCode(String bookingCode) {
        return bookingLogRepository.getLogsByBookingCode(bookingCode).stream().map(bookingLogMapper::toResponse).toList();
    }

    public String generatePaymentUrl(long bookingId, HttpServletRequest servletRequest) {
        long totalPrice = bookingRepository.getPriceByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));
        return vnPayService.generatePaymentUrl(new PaymentRequest(bookingId, totalPrice), servletRequest);
    }

    private Booking findByBookingCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode).orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));
    }


}