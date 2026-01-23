package org.foodie_tour.modules.tours.service;

import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;

public interface TourService {
    TourResponse createTour(TourRequest tourRequest);
}
