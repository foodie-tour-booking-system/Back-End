package org.foodie_tour.modules.schedules.dto.response;

import lombok.Builder;
import lombok.Data;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleResponse {

    private Long scheduleId;
    private Long tourId;
    private Long routeId;
    private String scheduleNote;
    private String scheduleDescription;
    private Integer duration;
    private Integer minPax;
    private Integer maxPax;
    private LocalDateTime departureAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ScheduleStatus scheduleStatus;
}
