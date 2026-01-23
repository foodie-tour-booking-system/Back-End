package org.foodie_tour.modules.routes.mapper;

import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.entity.RouteDetail;
import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.entity.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "routeDetails", source = "routeDetails")
    Route toEntity(RouteRequest routeRequest);

    @Mapping(target = "routeDetailId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "routeDetailStatus", constant = "ACTIVE")
    RouteDetail toDetailEntity(RouteDetail routeDetailRequest);

    @Mapping(target = "tourId", source = "tour.tourId")
    RouteResponse toResponse(Route route);
}
