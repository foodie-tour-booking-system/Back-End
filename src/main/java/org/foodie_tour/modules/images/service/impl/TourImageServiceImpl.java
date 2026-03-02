package org.foodie_tour.modules.images.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.images.dto.request.TourImageRequest;
import org.foodie_tour.modules.images.dto.response.TourImageResponse;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.images.entity.TourImage;
import org.foodie_tour.modules.images.mapper.TourImageMapper;
import org.foodie_tour.modules.images.repository.ImageRepository;
import org.foodie_tour.modules.images.repository.TourImageRepository;
import org.foodie_tour.modules.images.service.TourImageService;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourImageServiceImpl implements TourImageService {
    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;
    private final TourImageRepository tourImageRepository;
    private final TourImageMapper tourImageMapper;

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

    private void resetPrimaryStatus(Long tourId) {
        if (!tourRepository.existsById(tourId)) {
            throw new ResourceNotFoundException("Tour không tồn tại để đặt lại trạng thái ảnh");
        }
        tourImageRepository.resetPrimaryStatusByTourId(tourId);
    }
}