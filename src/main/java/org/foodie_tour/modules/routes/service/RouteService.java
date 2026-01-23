package org.foodie_tour.modules.routes.service;

import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;

public interface RouteService {
    RouteResponse createRoute(RouteRequest routeRequest);
}
