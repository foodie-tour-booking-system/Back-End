package org.foodie_tour.modules.images.mapper;

import org.foodie_tour.modules.images.dto.response.TourImageResponse;
import org.foodie_tour.modules.images.entity.TourImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourImageMapper {

    @Mapping(target = "imageId", source = "image.imageId")
    @Mapping(target = "imageUrl", source = "image.imageUrl")
    @Mapping(target = "status", source = "tourImageStatus")
    TourImageResponse toTourImageResponse(TourImage tourImage);
}