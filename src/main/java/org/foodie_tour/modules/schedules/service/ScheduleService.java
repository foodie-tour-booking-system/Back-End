package org.foodie_tour.modules.schedules.service;

import org.foodie_tour.modules.schedules.dto.request.ScheduleRequest;
import org.foodie_tour.modules.schedules.dto.response.ScheduleResponse;

public interface ScheduleService {
    ScheduleResponse createSchedule(ScheduleRequest request);
}
