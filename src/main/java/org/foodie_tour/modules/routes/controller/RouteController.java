package org.foodie_tour.modules.routes.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.foodie_tour.modules.routes.service.RouteService;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity<List<RouteResponse>> getAllRoutes(
            @RequestParam(required = false) RouteStatus routeStatus
            ) {
        List<RouteResponse> response = routeService.getAllRoutes(routeStatus);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable Long id,
            @RequestBody @Valid RouteRequest routeRequest
    ) {
        RouteResponse response = routeService.updateRouteById(id, routeRequest);
        return ResponseEntity.ok(response);
    }
}
