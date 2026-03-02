package org.foodie_tour.modules.images.controller;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;
import org.foodie_tour.modules.images.service.TourImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tour/{tourId}/images")
@RequiredArgsConstructor
public class TourImageController {

    private final TourImageService tourImageService;

    @PostMapping
    public ResponseEntity<TourImageResponse> addImage(
            @PathVariable Long tourId,
            @RequestBody TourImageRequest request) {
        return ResponseEntity.ok(tourImageService.addImageToTour(tourId, request));
    }
}
