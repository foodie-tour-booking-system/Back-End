package org.foodie_tour.modules.images.controller;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.images.dto.response.ImageResponse;
import org.foodie_tour.modules.images.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String description
    ) throws IOException {
        ImageResponse response = imageService.uploadImage(file, description);
        return ResponseEntity.ok(response);
    }
}
