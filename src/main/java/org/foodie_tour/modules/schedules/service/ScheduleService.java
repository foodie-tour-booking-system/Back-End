package org.foodie_tour.modules.schedules.service;

import java.util.List;
import org.foodie_tour.modules.schedules.dto.request.ScheduleRequest;
import org.foodie_tour.modules.schedules.dto.response.ScheduleResponse;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;

public interface ScheduleService {
    ScheduleResponse createSchedule(ScheduleRequest request);

    List<ScheduleResponse> getSchedules(Long tourId, Long routeId, ScheduleStatus status);
    ScheduleResponse updateSchedule(Long scheduleId, ScheduleRequest request);
    void deleteSchedule(Long scheduleId);
}
