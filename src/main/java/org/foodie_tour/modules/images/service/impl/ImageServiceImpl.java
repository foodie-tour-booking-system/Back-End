package org.foodie_tour.modules.images.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.aws.s3.service.S3Service;
import org.foodie_tour.modules.images.dto.response.ImageResponse;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.images.enums.ImageStatus;
import org.foodie_tour.modules.images.mapper.ImageMapper;
import org.foodie_tour.modules.images.repository.ImageRepository;
import org.foodie_tour.modules.images.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Override
    public ImageResponse uploadImage(MultipartFile file, String description) throws IOException {
        String publicUrl = s3Service.uploadFile(file);

        Image image = new Image();
        image.setImageUrl(publicUrl);
        image.setImageDescription(description);
        image.setImageStatus(ImageStatus.ACTIVE);
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());

        image = imageRepository.save(image);
        return imageMapper.toResponse(image);
    }

    @Override
    public List<ImageResponse> getAllImages(ImageStatus imageStatus) {
        List<Image> images;
        if (imageStatus != null) {
            images = imageRepository.findImageByImageStatus(imageStatus);
        } else {
            images = imageRepository.findAll();
        }
        return images.stream()
                .map(imageMapper::toResponse)
                .toList();
    }
}



