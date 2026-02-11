package org.foodie_tour.modules.schedules.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.repository.RouteRepository;
import org.foodie_tour.modules.schedules.dto.request.ScheduleRequest;
import org.foodie_tour.modules.schedules.dto.response.ScheduleResponse;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.mapper.ScheduleMapper;
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.schedules.service.ScheduleService;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final TourRepository tourRepository;
    private final RouteRepository routeRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleResponse createSchedule(ScheduleRequest request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Tuyến đường không tồn tại"));

        Schedule schedule = scheduleMapper.toEntity(request);
        schedule.setTour(tour);
        schedule.setRoute(route);
        schedule.setCreatedAt(LocalDateTime.now());
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(schedule);
    }
}
