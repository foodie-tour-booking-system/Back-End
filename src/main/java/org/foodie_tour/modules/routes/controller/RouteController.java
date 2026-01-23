package org.foodie_tour.modules.routes.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping()
    public ResponseEntity<RouteResponse> createRoute(@RequestBody @Valid RouteRequest routeRequest) {
        RouteResponse response = routeService.createRoute(routeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
