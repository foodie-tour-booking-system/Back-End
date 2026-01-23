package org.foodie_tour.modules.tours.repository;

import org.foodie_tour.modules.tours.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    boolean existsByTourName(String tourName);
}
