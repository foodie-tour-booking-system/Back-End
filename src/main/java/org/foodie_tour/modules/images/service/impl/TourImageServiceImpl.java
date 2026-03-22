package org.foodie_tour.modules.images.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.aws.s3.service.S3Service;
import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.images.entity.TourImage;
import org.foodie_tour.modules.images.enums.ImageStatus;
import org.foodie_tour.modules.images.enums.TourImageStatus;
import org.foodie_tour.modules.images.mapper.TourImageMapper;
import org.foodie_tour.modules.images.repository.ImageRepository;
import org.foodie_tour.modules.images.repository.TourImageRepository;
import org.foodie_tour.modules.images.service.TourImageService;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourImageServiceImpl implements TourImageService {
    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;
    private final TourImageRepository tourImageRepository;
    private final TourImageMapper tourImageMapper;
    private final S3Service s3Service;

    @Override
    @Transactional
    public TourImageResponse addImageToTour(Long tourId, TourImageRequest request) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        Image image = imageRepository.findById(request.getImageId())
                .orElseThrow(() -> new ResourceNotFoundException("Hình ảnh không tồn tại"));

        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            resetPrimaryStatus(tourId);
        }

        TourImage tourImage = new TourImage();
        tourImage.setTour(tour);
        tourImage.setImage(image);
        tourImage.setImageUrl(image.getImageUrl());
        tourImage.setIsPrimary(request.getIsPrimary());
        tourImage.setDisplayOrder(request.getDisplayOrder());
        tourImage.setTourImageStatus(request.getStatus());

        TourImage savedImage = tourImageRepository.save(tourImage);

        return tourImageMapper.toTourImageResponse(savedImage);
    }

    @Override
    public List<TourImageResponse> getTourImages(Long tourId) {
        return tourImageRepository.findByTourId(tourId)
                .stream()
                .map(img -> TourImageResponse.builder()
                        .imageId(img.getImage().getImageId())
                        .tourImageId(img.getTourImageId())
                        .imageUrl(img.getImageUrl())
                        .isPrimary(img.getIsPrimary())
                        .displayOrder(img.getDisplayOrder())
                        .status(img.getTourImageStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setPrimaryImage(Long tourId, Long tourImageId) {
        List<TourImage> images = tourImageRepository.findByTourId(tourId);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy hình ảnh");
        }

        boolean found = false;
        for (TourImage img : images) {
            if (img.getTourImageId().equals(tourImageId)) {
                img.setIsPrimary(true);
                found = true;
            } else {
                img.setIsPrimary(false);
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("Không tìm thấy hình ảnh trong tour này");
        }
        tourImageRepository.saveAll(images);
    }

    @Override
    @Transactional
    public TourImageResponse uploadImageToTour(Long tourId, MultipartFile file, Boolean isPrimary, int displayOrder) throws IOException {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        String publicUrl = s3Service.uploadFileWithCustomPrefix(file, "tours");

        Image image = new Image();
        image.setImageUrl(publicUrl);
        image.setImageStatus(ImageStatus.ACTIVE);
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());
        image = imageRepository.save(image);

        if (Boolean.TRUE.equals(isPrimary)) {
            tourImageRepository.resetPrimaryStatusByTourId(tourId);
        }

        TourImage tourImage = new TourImage();
        tourImage.setTour(tour);
        tourImage.setImage(image);
        tourImage.setImageUrl(publicUrl);
        tourImage.setIsPrimary(isPrimary != null && isPrimary);
        tourImage.setDisplayOrder(displayOrder);
        tourImage.setTourImageStatus(TourImageStatus.ACTIVE);
        tourImage.setCreatedAt(LocalDateTime.now());
        tourImage.setUpdatedAt(LocalDateTime.now());

        TourImage saved = tourImageRepository.save(tourImage);
        return tourImageMapper.toTourImageResponse(saved);
    }

    private void resetPrimaryStatus(Long tourId) {
        if (!tourRepository.existsById(tourId)) {
            throw new ResourceNotFoundException("Tour không tồn tại để đặt lại trạng thái ảnh");
        }
        tourImageRepository.resetPrimaryStatusByTourId(tourId);
    }
}