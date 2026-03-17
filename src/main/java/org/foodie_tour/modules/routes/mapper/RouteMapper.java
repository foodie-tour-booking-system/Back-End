package org.foodie_tour.modules.routes.mapper;

import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "routeDetails", source = "routeDetails")
    Route toEntity(RouteRequest routeRequest);

    @Mapping(target = "tourId", source = "tour.tourId")
    RouteResponse toResponse(Route route);

    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "routeDetails", ignore = true)
    void updateEntity(RouteRequest routeRequest, @MappingTarget Route route);
}
