package org.foodie_tour.modules.routes.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.foodie_tour.modules.routes.mapper.RouteMapper;
import org.foodie_tour.modules.routes.repository.RouteDetailRepository;
import org.foodie_tour.modules.routes.repository.RouteRepository;
import org.foodie_tour.modules.routes.service.RouteService;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final TourRepository tourRepository;
    private final RouteDetailRepository routeDetailRepository;

    @Override
    @Transactional
    public RouteResponse createRoute(RouteRequest routeRequest) {
        if (routeRepository.existsByRouteName(routeRequest.getRouteName())) {
            throw new DuplicateResourceException("Tuyến đường này đã tồn tại");
        }

        Tour tour = tourRepository.findById(routeRequest.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));


        Route route = routeMapper.toEntity(routeRequest);
        route.setTour(tour);
        route.setCreatedAt(LocalDateTime.now());

        if (route.getRouteDetails() != null) {
            route.getRouteDetails().forEach(routeDetail -> {
                routeDetail.setRoute(route);
                routeDetail.setCreatedAt(LocalDateTime.now());
            });
        }
        routeRepository.save(route);
        return routeMapper.toResponse(route);
    }

    @Override
    public List<RouteResponse> getAllRoutes(RouteStatus routeStatus) {
        List<Route> routes;
        if (routeStatus != null) {
            routes = routeRepository.findRouteByRouteStatus(routeStatus);
        } else {
            routes = routeRepository.findAll();
        }

        return routes.stream()
                .map(routeMapper::toResponse)
                .toList();
    }
}
