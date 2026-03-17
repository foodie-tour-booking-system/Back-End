package org.foodie_tour.modules.tours.dto.response;

import lombok.Builder;
import lombok.Data;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.enums.DishType;

import java.time.LocalDateTime;

@Data
@Builder
public class DishResponse {
    private Long tourId;
    private Long dishId;
    private String dishName;
    private String dishDescription;
    private Boolean isPrimary;
    private DishType dishType;
    private DishStatus dishStatus;
    private LocalDateTime createdAt;
}
