package org.foodie_tour.modules.routes.repository;

import org.foodie_tour.modules.routes.entity.RouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteDetailRepository extends JpaRepository<RouteDetail, Long> {
}
