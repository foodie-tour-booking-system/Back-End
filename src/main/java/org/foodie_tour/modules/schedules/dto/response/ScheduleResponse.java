package org.foodie_tour.modules.schedules.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private Integer minPax;
    private Integer maxPax;
    @Schema(type = "string", example = "2026-01-01T08:00:00", description = "Thời điểm khởi hành (yyyy-MM-dd'T'HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ScheduleStatus scheduleStatus;
}
