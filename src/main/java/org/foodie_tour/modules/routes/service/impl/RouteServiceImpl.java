package org.foodie_tour.modules.routes.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.rag.RagUtils;
import org.foodie_tour.modules.routes.dto.request.RouteRequest;
import org.foodie_tour.modules.routes.dto.response.RouteResponse;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.foodie_tour.modules.routes.mapper.RouteMapper;
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
    private final RagUtils ragUtils;

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

        ragUtils.updateVectorTour(tour);

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

    @Override
    public RouteResponse getRouteById(Long routeId, RouteStatus routeStatus) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tuyến đường không tồn tại"));

        if (routeStatus != null && !route.getRouteStatus().equals(routeStatus)) {
            throw new ResourceNotFoundException("Tuyến đường không tồn tại với trạng thái");
        }

        return routeMapper.toResponse(route);
    }

    @Override
    public RouteResponse updateRouteById(Long routeId, RouteRequest routeRequest) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tuyến đường không tồn tại"));

        Tour tour = tourRepository.findById(routeRequest.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        routeMapper.updateEntity(routeRequest, route);
        route.setUpdatedAt(LocalDateTime.now());
        route.setTour(tour);

        if (route.getRouteDetails() != null) {
            route.getRouteDetails().forEach(routeDetail -> {
                routeDetail.setRoute(route);
                routeDetail.setUpdatedAt(LocalDateTime.now());
            });
        }
        routeRepository.save(route);

        ragUtils.updateVectorTour(tour);

        return routeMapper.toResponse(route);
    }

    @Override
    public void deleteRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tuyến đường"));
        route.setRouteStatus(RouteStatus.DELETED);
        routeRepository.save(route);

        Tour tour = route.getTour();
        ragUtils.updateVectorTour(tour);
    }

    @Override
    public List<RouteResponse> getRouteByTourId(Long tourId, RouteStatus routeStatus) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        List<Route> route = routeRepository.findRouteByTour(tour);


        return route.stream()
                .filter(r -> routeStatus == null || r.getRouteStatus().equals(routeStatus))
                .map(routeMapper::toResponse)
                .toList();
    }
}
