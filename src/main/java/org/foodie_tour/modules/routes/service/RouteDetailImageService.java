package org.foodie_tour.modules.routes.service;

import org.foodie_tour.modules.routes.dto.response.RouteDetailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RouteDetailImageService {
    RouteDetailResponse uploadImagesToRouteDetail(Long routeDetailId, List<MultipartFile> files) throws IOException;
    RouteDetailResponse getRouteDetailWithImages(Long routeDetailId);
}
