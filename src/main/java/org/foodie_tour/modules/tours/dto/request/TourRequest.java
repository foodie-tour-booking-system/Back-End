package org.foodie_tour.modules.tours.dto.request;

import lombok.Data;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.foodie_tour.modules.tours.enums.TourType;

import java.time.LocalDateTime;

@Data
public class TourRequest {
    private String tourName;
    private String tourDescription;
    private Long basePriceAdult;
    private Long basePriceChild;
    private int duration;
    private TourType tourType;
    private TourStatus tourStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
