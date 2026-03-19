package org.foodie_tour.modules.booking.controller;
import org.foodie_tour.modules.tracking.TrackingService;
import org.foodie_tour.modules.tracking.dto.TrackingResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.dto.request.BookingCancelRequest;
import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.request.ProcessRelocateRequest;
import org.foodie_tour.modules.booking.dto.request.RelocateBookingRequest;
import org.foodie_tour.modules.booking.dto.request.RescheduleRequest;
import org.foodie_tour.modules.booking.dto.response.BookingLogResponse;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;
import org.foodie_tour.modules.booking.dto.response.RelocateBookingResponse;
import org.foodie_tour.modules.booking.enums.PaymentMethod;
import org.foodie_tour.modules.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/booking")
public class BookingController {
    BookingService bookingService;
    TrackingService trackingService;

    @PostMapping("/reschedule")
    public ResponseEntity<String> rescheduleBooking(@RequestBody RescheduleRequest request) {
        String result = bookingService.rescheduleBooking(request);
        return ResponseEntity.ok(result);
    }

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

    @GetMapping("/relocate/all-request")
    public ResponseEntity<List<RelocateBookingResponse>> getAllRelocateRequest() {
        var result = bookingService.getAllPendingRelocateRequest();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/{bookingCode}/relocate")
    public ResponseEntity<String> relocateBooking(@PathVariable String bookingCode) {
        var result = bookingService.requestRelocateBooking(bookingCode);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/relocate/verify")
    public ResponseEntity<String> verifyBooking(@RequestHeader (value = "Access-Token") String accessToken, @RequestBody RelocateBookingRequest request) {
        bookingService.createRelocateBookingRequest(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("Tạo yêu cầu thành công");
    }

    @PutMapping("/relocate/process")
    @PreAuthorize("hasAuthority('PROCESS_RELOCATE_BOOKING_REQUEST')")
    public ResponseEntity<BookingResponse> processRequest(@RequestBody ProcessRelocateRequest request) {
        var result = bookingService.processRelocateRequest(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestBody BookingCancelRequest request) {
        String result = bookingService.cancelBooking(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{bookingCode}/approve-refund")
    public ResponseEntity<String> approveManualRefund(
            @PathVariable String bookingCode
    ) {
        String result = bookingService.approveManualRefund(bookingCode);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{bookingCode}/complete-payment")
//    @PreAuthorize("hasAuthority('APPROVE_PAYMENT')")
    public ResponseEntity<BookingResponse> completePayment(
            @PathVariable String bookingCode,
            @RequestParam PaymentMethod method
    ) {
        var result = bookingService.completeOnTourPayment(bookingCode, method);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{bookingCode}/tracking")
    public ResponseEntity<TrackingResponse> trackBooking(@PathVariable String bookingCode) {
        return ResponseEntity.ok(trackingService.trackBooking(bookingCode));
    }
}