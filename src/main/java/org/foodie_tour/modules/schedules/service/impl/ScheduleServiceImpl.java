package org.foodie_tour.modules.schedules.service.impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.repository.RouteRepository;
import org.foodie_tour.modules.schedules.dto.request.ScheduleRequest;
import org.foodie_tour.modules.schedules.dto.response.ScheduleResponse;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.foodie_tour.modules.schedules.mapper.ScheduleMapper;
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.schedules.service.ScheduleService;
import org.foodie_tour.modules.schedules.specifications.ScheduleSpecification;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final TourRepository tourRepository;
    private final RouteRepository routeRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public ScheduleResponse createSchedule(ScheduleRequest request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Tuyến đường không tồn tại"));

        if (request.getMinPax() > request.getMaxPax()) {
            throw new InvalidateDataException(
                    "Số lượng khách tối thiểu không được lớn hơn số lượng khách tối đa");
        }

        Schedule schedule = scheduleMapper.toEntity(request);
        schedule.setTour(tour);
        schedule.setRoute(route);
        schedule.setCreatedAt(LocalDateTime.now());
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(Long tourId, Long routeId, ScheduleStatus status) {
        Specification<Schedule> spec = Specification.where(ScheduleSpecification.hasTourId(tourId))
                .and(ScheduleSpecification.hasRouteId(routeId))
                .and(ScheduleSpecification.hasStatus(status));
        return scheduleRepository.findAll(spec)
                .stream()
                .map(scheduleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Lịch trình không tồn tại"));

        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Tuyến đường không tồn tại"));

        if (request.getMinPax() > request.getMaxPax()) {
            throw new InvalidateDataException(
                    "Số lượng khách tối thiểu không được lớn hơn số lượng khách tối đa");
        }

        scheduleMapper.updateEntity(request, schedule);
        schedule.setTour(tour);
        schedule.setRoute(route);
        schedule.setUpdatedAt(LocalDateTime.now());
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Lịch trình không tồn tại"));
        schedule.setScheduleStatus(ScheduleStatus.INACTIVE);
        scheduleRepository.save(schedule);
    }
}