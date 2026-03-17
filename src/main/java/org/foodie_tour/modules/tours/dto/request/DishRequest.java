package org.foodie_tour.modules.tours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.enums.DishType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishRequest {

    private Long tourId;
    private String dishName;
    private String dishDescription;
    private Boolean isPrimary;
    private DishType dishType;
    private DishStatus dishStatus;
    private LocalDateTime createdAt;
    private Long imageId;
}
