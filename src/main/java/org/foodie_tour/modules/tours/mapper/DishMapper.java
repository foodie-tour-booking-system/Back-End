package org.foodie_tour.modules.tours.mapper;

import org.foodie_tour.modules.tours.dto.request.DishRequest;
import org.foodie_tour.modules.tours.dto.response.DishResponse;
import org.foodie_tour.modules.tours.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(target = "dishId", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Dish toEntity(DishRequest dishRequest);

    @Mapping(target = "tourId", source = "tour.tourId")
    DishResponse toResponse(Dish dish);

    @Mapping(target = "dishId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(DishRequest dishRequest, @MappingTarget Dish dish);
}
