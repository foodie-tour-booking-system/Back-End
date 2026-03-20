package org.foodie_tour.modules.tours.repository;

import org.foodie_tour.modules.tours.entity.Dish;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByDishName(String dishName);
    List<Dish> findByDishStatus(DishStatus dishStatus);

    @Query("SELECT d FROM Dish d WHERE d.tour.tourId = :tourId")
    List<Dish> findByTourId(@Param(value = "tourId") long tourId);
}
