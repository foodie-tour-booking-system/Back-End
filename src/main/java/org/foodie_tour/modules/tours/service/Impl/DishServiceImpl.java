package org.foodie_tour.modules.tours.service.Impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.tours.dto.request.DishRequest;
import org.foodie_tour.modules.tours.dto.response.DishResponse;
import org.foodie_tour.modules.tours.entity.Dish;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.mapper.DishMapper;
import org.foodie_tour.modules.tours.repository.DishRepository;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.foodie_tour.modules.tours.service.DishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;
    private final TourRepository tourRepository;

    @Override
    @Transactional
    public DishResponse createDish(DishRequest dishRequest) {
        if (dishRepository.existsByDishName(dishRequest.getDishName())) {
            throw new DuplicateResourceException("Trùng tên món ăn");
        }

        Tour tour = tourRepository.findById(dishRequest.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        Dish dish = dishMapper.toEntity(dishRequest);
        dish.setTour(tour);
        dish.setCreatedAt(LocalDateTime.now());
        dish = dishRepository.save(dish);
        return dishMapper.toResponse(dish);
    }
}
