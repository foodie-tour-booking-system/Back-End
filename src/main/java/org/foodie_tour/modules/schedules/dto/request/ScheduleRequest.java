package org.foodie_tour.modules.schedules.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {

    private Long tourId;
    private Long routeId;
    private String scheduleNote;
    private String scheduleDescription;
    private Integer duration;
    private Integer minPax;
    private Integer maxPax;
    private LocalDateTime departureAt;
    private ScheduleStatus scheduleStatus;
}
