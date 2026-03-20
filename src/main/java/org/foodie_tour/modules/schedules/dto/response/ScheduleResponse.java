package org.foodie_tour.modules.schedules.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    private ScheduleStatus scheduleStatus;
    
    @Schema(type = "string", example = "08:00:00", description = "Thời gian khởi hành (HH:mm:ss)")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    
    private Boolean isTemplate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
