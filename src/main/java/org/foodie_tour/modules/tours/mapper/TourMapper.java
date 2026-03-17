package org.foodie_tour.modules.tours.mapper;

import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.entity.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TourMapper {

    @Mapping(target = "tourId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tour toEntity(TourRequest tourRequest);

    TourResponse toResponse(Tour tour);

    @Mapping(target = "tourId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(TourRequest tourRequest, @MappingTarget Tour tour);
}
