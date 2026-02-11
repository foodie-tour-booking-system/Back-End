package org.foodie_tour.modules.tours.repository;

import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {
    boolean existsByTourName(String tourName);

    List<Tour> findByTourStatus(TourStatus tourStatus);
}
