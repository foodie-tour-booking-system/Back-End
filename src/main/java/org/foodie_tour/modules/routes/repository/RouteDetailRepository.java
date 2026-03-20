package org.foodie_tour.modules.routes.repository;

import org.foodie_tour.modules.routes.entity.RouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteDetailRepository extends JpaRepository<RouteDetail, Long> {

    @Query("SELECT r FROM RouteDetail r WHERE r.route.tour.tourId = :tourId")
    List<RouteDetail> findAllByTourId(@Param(value = "tourId") long tourId);
}
