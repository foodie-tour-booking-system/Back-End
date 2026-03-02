
package org.foodie_tour.modules.booking.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.response.BookingLogResponse;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;
import org.foodie_tour.modules.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/booking")
public class BookingController {
    BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingCreateRequest request) {
        var result = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{bookingCode}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String bookingCode) {
        var result = bookingService.getBookingByCode(bookingCode);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{bookingCode}/logs")
    public ResponseEntity<List<BookingLogResponse>> getLogsByBookingId(@PathVariable String bookingCode) {
        var result = bookingService.getLogsByBookingCode(bookingCode);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/{bookingId}/payment")
    public ResponseEntity<String> generatePaymentUrl(@PathVariable long bookingId, HttpServletRequest servletRequest) {
        var result = bookingService.generatePaymentUrl(bookingId, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}