package org.foodie_tour.modules.tours.service;

import org.foodie_tour.modules.tours.dto.request.DishRequest;
import org.foodie_tour.modules.tours.dto.response.DishResponse;

public interface DishService {

    DishResponse createDish(DishRequest dishRequest);
}
