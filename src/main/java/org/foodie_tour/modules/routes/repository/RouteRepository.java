package org.foodie_tour.modules.routes.repository;

import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    boolean existsByRouteName(String routeName);
    List<Route> findRouteByRouteStatus(RouteStatus routeStatus);
}
