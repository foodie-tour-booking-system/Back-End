package org.foodie_tour.modules.tours.mapper;

import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.entity.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourMapper {

    @Mapping(target = "tourId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Tour toEntity(TourRequest tourRequest);

    TourResponse toResponse(Tour tour);

}
