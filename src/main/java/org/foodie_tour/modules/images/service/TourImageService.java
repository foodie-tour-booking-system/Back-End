package org.foodie_tour.modules.images.service;

import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;

import java.util.List;

public interface TourImageService {
    TourImageResponse addImageToTour(Long tourId, TourImageRequest request);
    List<TourImageResponse> getTourImages(Long tourId);
    void setPrimaryImage(Long tourId, Long tourImageId);
}
