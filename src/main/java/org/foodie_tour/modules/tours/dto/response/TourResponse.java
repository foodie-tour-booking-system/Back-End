package org.foodie_tour.modules.tours.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.foodie_tour.modules.tours.enums.TourType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourResponse {
    private Long tourId;
    private String tourName;
    private String tourDescription;
    private Long groupPriceAdult;
    private Long groupPriceChild;
    private Long privatePriceAdult;
    private Long privatePriceChild;
    private int duration;

    private TourStatus tourStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
