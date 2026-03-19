package org.foodie_tour.modules.routes.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.aws.s3.service.S3Service;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.images.enums.ImageStatus;
import org.foodie_tour.modules.images.repository.ImageRepository;
import org.foodie_tour.modules.routes.dto.response.RouteDetailResponse;
import org.foodie_tour.modules.routes.entity.RouteDetail;
import org.foodie_tour.modules.routes.mapper.RouteMapper;
import org.foodie_tour.modules.routes.repository.RouteDetailRepository;
import org.foodie_tour.modules.routes.service.RouteDetailImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteDetailImageServiceImpl implements RouteDetailImageService {

    private final RouteDetailRepository routeDetailRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private final RouteMapper routeMapper;

    @Override
    @Transactional
    public RouteDetailResponse uploadImagesToRouteDetail(Long routeDetailId, List<MultipartFile> files) throws IOException {
        RouteDetail routeDetail = routeDetailRepository.findById(routeDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkpoint không tồn tại"));

        List<Image> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String publicUrl = s3Service.uploadFileWithCustomPrefix(file, "checkpoints");

            Image image = new Image();
            image.setImageUrl(publicUrl);
            image.setImageStatus(ImageStatus.ACTIVE);
            image.setCreatedAt(LocalDateTime.now());
            image.setUpdatedAt(LocalDateTime.now());
            image.setRouteDetail(routeDetail);

            savedImages.add(imageRepository.save(image));
        }

        // Thêm vào danh sách hiện có (tránh replace lazy list)
        if (routeDetail.getImages() == null) {
            routeDetail.setImages(new ArrayList<>());
        }
        routeDetail.getImages().addAll(savedImages);

        return routeMapper.toRouteDetailResponse(routeDetail);
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDetailResponse getRouteDetailWithImages(Long routeDetailId) {
        RouteDetail routeDetail = routeDetailRepository.findById(routeDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkpoint không tồn tại"));
        return routeMapper.toRouteDetailResponse(routeDetail);
    }
}
