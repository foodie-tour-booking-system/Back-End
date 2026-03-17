package org.foodie_tour.modules.images.dto.response;

import lombok.Builder;
import lombok.Data;
import org.foodie_tour.modules.images.enums.ImageStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ImageResponse {
    private Long imageId;
    private String imageUrl;
    private String imageDescription;
    private ImageStatus imageStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}