package org.foodie_tour.modules.images.service;

import org.foodie_tour.modules.images.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    ImageResponse uploadImage(MultipartFile file, String description) throws IOException;
}
