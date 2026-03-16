package org.foodie_tour.modules.schedules.mapper;

import org.foodie_tour.modules.schedules.dto.request.ScheduleRequest;
import org.foodie_tour.modules.schedules.dto.response.ScheduleResponse;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScheduleMapper {

    @Mapping(target = "scheduleId", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Schedule toEntity(ScheduleRequest scheduleRequest);

    @Mapping(target = "tourId", source = "tour.tourId")
    @Mapping(target = "routeId", source = "route.routeId")
    ScheduleResponse toResponse(Schedule schedule);

    @Mapping(target = "scheduleId", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ScheduleRequest scheduleRequest, @MappingTarget Schedule schedule);
}
