package org.foodie_tour.modules.images.service;

import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;

public interface TourImageService {
    TourImageResponse addImageToTour(Long tourId, TourImageRequest request);

}
