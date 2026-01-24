package org.foodie_tour.modules.tours.service;

import org.foodie_tour.modules.tours.dto.request.DishRequest;
import org.foodie_tour.modules.tours.dto.response.DishResponse;
import org.foodie_tour.modules.tours.enums.DishStatus;

import java.util.List;

public interface DishService {

    DishResponse createDish(DishRequest dishRequest);
    List<DishResponse> getAllDishes(DishStatus dishStatus);
}
