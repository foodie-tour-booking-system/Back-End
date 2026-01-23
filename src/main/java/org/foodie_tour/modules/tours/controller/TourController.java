package org.foodie_tour.modules.tours.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.foodie_tour.modules.tours.service.TourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @PostMapping()
    public ResponseEntity<TourResponse> createTour(@Valid @RequestBody TourRequest tourRequest) {
        TourResponse response = tourService.createTour(tourRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<TourResponse>> getAllTours(
            @RequestParam(required = false) TourStatus status) {
        List<TourResponse> tours = tourService.getAllTours(status);
        return ResponseEntity.status(HttpStatus.OK).body(tours);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourResponse> getTourById(
            @RequestParam("id")
            @PathVariable Long id
    ) {
        TourResponse response = tourService.getTourById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{tourId}")
    public ResponseEntity<TourResponse> updateTour(
            @PathVariable Long tourId,
            @Valid @RequestBody TourRequest tourRequest
            ) {
        TourResponse response = tourService.updateTour(tourId, tourRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{tourId}")
    public ResponseEntity<String> deleteTour(
            @PathVariable Long tourId
    ) {
        tourService.deleteTour(tourId);
        return ResponseEntity.ok("Xóa tour thành công");
    }
}
