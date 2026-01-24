package org.foodie_tour.modules.tours.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.tours.dto.request.DishRequest;
import org.foodie_tour.modules.tours.dto.response.DishResponse;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping("")
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest dishRequest) {
        DishResponse response = dishService.createDish(dishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<DishResponse>> getAllDishes(
            @RequestParam(required = false)
            DishStatus dishStatus
    ) {
        List<DishResponse> response = dishService.getAllDishes(dishStatus);
        return ResponseEntity.ok(response);
    }
}
