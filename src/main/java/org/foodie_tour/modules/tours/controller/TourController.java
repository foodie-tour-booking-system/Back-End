package org.foodie_tour.modules.tours.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.foodie_tour.modules.tours.service.TourService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_TOUR')")
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
    @PreAuthorize("hasAuthority('UPDATE_TOUR')")
    public ResponseEntity<TourResponse> updateTour(
            @PathVariable Long tourId,
            @Valid @RequestBody TourRequest tourRequest
            ) {
        TourResponse response = tourService.updateTour(tourId, tourRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{tourId}")
    @PreAuthorize("hasAuthority('DELETE_TOUR')")
    public ResponseEntity<String> deleteTour(
            @PathVariable Long tourId
    ) {
        tourService.deleteTour(tourId);
        return ResponseEntity.ok("Xóa tour thành công");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TourResponse>> searchTour(
            @RequestParam(required = false) String tourName,
            @RequestParam(required = false) TourStatus status,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(tourService.searchTours(tourName, status, minPrice, maxPrice, pageable));
    }
}
