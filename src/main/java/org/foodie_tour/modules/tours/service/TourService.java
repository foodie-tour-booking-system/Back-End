package org.foodie_tour.modules.tours.service;

import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TourService {
    TourResponse createTour(TourRequest tourRequest);
    List<TourResponse> getAllTours(TourStatus tourStatus);
    TourResponse getTourById(Long tourId);
    TourResponse updateTour(Long tourId, TourRequest tourRequest);
    void deleteTour(Long tourId);
    Page<TourResponse> searchTours(String name, TourStatus status, Long minPrice, Long maxPrice, Pageable pageable);
}
