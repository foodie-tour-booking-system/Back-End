package org.foodie_tour.modules.tours.repository;

import org.foodie_tour.modules.tours.entity.Dish;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByDishName(String dishName);
    List<Dish> findByDishStatus(DishStatus dishStatus);
}
