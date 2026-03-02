package org.foodie_tour.modules.images.dto.request;

import lombok.Data;
import org.foodie_tour.modules.images.enums.TourImageStatus;

@Data
public class TourImageRequest {

    private Long imageId;
    private Boolean isPrimary;
    private int displayOrder;
    private TourImageStatus status;
}