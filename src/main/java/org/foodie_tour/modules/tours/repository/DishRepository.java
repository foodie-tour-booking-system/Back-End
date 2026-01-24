package org.foodie_tour.modules.tours.repository;

import org.foodie_tour.modules.tours.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByDishName(String dishName);
}
