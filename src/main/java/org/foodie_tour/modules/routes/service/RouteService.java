package org.foodie_tour.modules.routes.service;

import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;

import org.foodie_tour.modules.routes.enums.RouteStatus;

import java.util.List;

public interface RouteService {
    RouteResponse createRoute(RouteRequest routeRequest);

    List<RouteResponse> getAllRoutes(RouteStatus routeStatus);

    RouteResponse getRouteById(Long routeId, RouteStatus routeStatus);

    RouteResponse updateRouteById(Long routeId, RouteRequest routeRequest);
}
