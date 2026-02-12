package org.foodie_tour.modules.images.service;

import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.ImageResponse;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;
import org.foodie_tour.modules.images.entity.TourImage;
import org.foodie_tour.modules.images.enums.ImageStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    ImageResponse uploadImage(MultipartFile file, String description) throws IOException;
    List<ImageResponse> getAllImages(ImageStatus imageStatus);
    ImageResponse getImageById(Long imageId);
}
