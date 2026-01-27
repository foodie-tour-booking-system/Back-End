package org.foodie_tour.modules.images.mapper;

import org.foodie_tour.modules.images.dto.response.ImageResponse;
import org.foodie_tour.modules.images.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "imageId", source = "imageId")
    @Mapping(target = "imageUrl", source = "imageUrl")
    ImageResponse toResponse(Image image);


}
