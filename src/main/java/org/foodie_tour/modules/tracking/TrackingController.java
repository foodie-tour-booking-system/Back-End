package org.foodie_tour.modules.tracking;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.tracking.dto.TrackingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping("/{bookingCode}")
    public ResponseEntity<TrackingResponse> trackBooking(@PathVariable String bookingCode) {
        return ResponseEntity.ok(trackingService.trackBooking(bookingCode));
    }
}

