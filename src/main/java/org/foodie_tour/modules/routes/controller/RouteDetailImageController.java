package org.foodie_tour.modules.routes.controller;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.routes.dto.response.RouteDetailResponse;
import org.foodie_tour.modules.routes.service.RouteDetailImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/routes/details")
@RequiredArgsConstructor
public class RouteDetailImageController {

    private final RouteDetailImageService routeDetailImageService;

    @PostMapping(value = "/{routeDetailId}/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RouteDetailResponse> uploadImages(
            @PathVariable Long routeDetailId,
            @RequestPart("files") List<MultipartFile> files
    ) throws IOException {
        RouteDetailResponse response = routeDetailImageService.uploadImagesToRouteDetail(routeDetailId, files);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{routeDetailId}")
    public ResponseEntity<RouteDetailResponse> getRouteDetail(@PathVariable Long routeDetailId) {
        return ResponseEntity.ok(routeDetailImageService.getRouteDetailWithImages(routeDetailId));
    }
}
