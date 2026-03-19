package org.foodie_tour.modules.images.controller;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;
import org.foodie_tour.modules.images.service.TourImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tour/{tourId}/images")
@RequiredArgsConstructor
public class TourImageController {

    private final TourImageService tourImageService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_TOUR_IMAGE')")
    public ResponseEntity<TourImageResponse> addImage(
            @PathVariable Long tourId,
            @RequestBody TourImageRequest request) {
        return ResponseEntity.ok(tourImageService.addImageToTour(tourId, request));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('ADD_TOUR_IMAGE')")
    public ResponseEntity<TourImageResponse> uploadImage(
            @PathVariable Long tourId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "false") Boolean isPrimary,
            @RequestParam(required = false, defaultValue = "0") int displayOrder
    ) throws IOException {
        return ResponseEntity.ok(tourImageService.uploadImageToTour(tourId, file, isPrimary, displayOrder));
    }

    @GetMapping
    public ResponseEntity<List<TourImageResponse>> getImages(@PathVariable Long tourId) {
        return ResponseEntity.ok(tourImageService.getTourImages(tourId));
    }

    @PatchMapping("/{tourImageId}/set-primary")
    @PreAuthorize("hasAuthority('SET_PRIMARY_TOUR_IMAGE')")
    public ResponseEntity<String> setPrimary(
            @PathVariable Long tourId,
            @PathVariable Long tourImageId
    ) {
        tourImageService.setPrimaryImage(tourId, tourImageId);
        return ResponseEntity.ok("Chỉnh sửa hình ảnh thành công");
    }
}

