
package org.foodie_tour.modules.booking.service;

import jakarta.servlet.http.HttpServletRequest;
import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.request.ProcessRelocateRequest;
import org.foodie_tour.modules.booking.dto.request.RelocateBookingRequest;
import org.foodie_tour.modules.booking.dto.response.BookingLogResponse;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;
import org.foodie_tour.modules.booking.dto.response.RelocateBookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest request);
    BookingResponse getBookingByCode(String id);
    List<BookingLogResponse> getLogsByBookingCode(String bookingId);
    String generatePaymentUrl(long bookingId, HttpServletRequest servletRequest);
    String requestRelocateBooking(String bookingCode);
    void createRelocateBookingRequest(String token,  RelocateBookingRequest request);
    List<RelocateBookingResponse> getAllPendingRelocateRequest();
    BookingResponse processRelocateRequest(ProcessRelocateRequest request);
}