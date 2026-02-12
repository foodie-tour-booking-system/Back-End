package org.foodie_tour.modules.images.dto.response;

import lombok.Builder;
import lombok.Data;
import org.foodie_tour.modules.images.enums.TourImageStatus;

@Builder
@Data
public class TourImageResponse {
    private Long tourImageId;
    private Long imageId;
    private String imageUrl;
    private Boolean isPrimary;
    private int displayOrder;
    private TourImageStatus status;
}
