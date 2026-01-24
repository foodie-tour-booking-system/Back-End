package org.foodie_tour.modules.tours.service.Impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.tours.dto.request.DishRequest;
import org.foodie_tour.modules.tours.dto.response.DishResponse;
import org.foodie_tour.modules.tours.entity.Dish;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.mapper.DishMapper;
import org.foodie_tour.modules.tours.repository.DishRepository;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.foodie_tour.modules.tours.service.DishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public List<DishResponse> getAllDishes(DishStatus dishStatus) {
        List<Dish> dishes;
        if (dishStatus != null) {
            dishes = dishRepository.findByDishStatus(dishStatus);
        } else {
            dishes = dishRepository.findAll();
        }

        return dishes.stream()
                .map(dishMapper::toResponse)
                .toList();
    }

    @Override
    public DishResponse getDishById(Long dishId, DishStatus dishStatus) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Món ăn không tồn tại"));

        if (dishStatus != null && !dish.getDishStatus().equals(dishStatus)) {
            throw new ResourceNotFoundException("Món ăn không tồn tại với trạng thái");
        }
        return dishMapper.toResponse(dish);
    }

    @Override
    public DishResponse updateDish(Long dishId, DishRequest dishRequest) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Món ăn không tồn tại"));

        dishMapper.updateEntity(dishRequest, dish);
        dish.setUpdatedAt(LocalDateTime.now());
        dish = dishRepository.save(dish);
        return dishMapper.toResponse(dish);
    }

    @Override
    public void deleteDish(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Món ăn không tồn tại"));
        dish.setDishStatus(DishStatus.INACTIVE);
        dishRepository.save(dish);
    }
}
