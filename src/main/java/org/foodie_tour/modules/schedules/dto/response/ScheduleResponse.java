package org.foodie_tour.modules.schedules.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class ScheduleResponse {

    private Long scheduleId;
    private Long tourId;
    private Long routeId;
    private String scheduleNote;
    private String scheduleDescription;
    private Integer minPax;
    private Integer maxPax;
    @Schema(type = "string", example = "08:00:00", description = "Giờ khởi hành (HH:mm:ss)")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ScheduleStatus scheduleStatus;
}
