package org.foodie_tour.modules.routes.mapper;

import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteDetailResponse;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.entity.RouteDetail;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "routeDetails", source = "routeDetails")
    Route toEntity(RouteRequest routeRequest);

    @Mapping(target = "tourId", source = "tour.tourId")
    @Mapping(target = "routeDetails", source = "routeDetails")
    RouteResponse toResponse(Route route);

    @Mapping(target = "imageUrls", source = "images", qualifiedByName = "imagesToUrls")
    RouteDetailResponse toRouteDetailResponse(RouteDetail routeDetail);

    @Named("imagesToUrls")
    default List<String> imagesToUrls(List<Image> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
    }

    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "routeDetails", ignore = true)
    void updateEntity(RouteRequest routeRequest, @MappingTarget Route route);
}

