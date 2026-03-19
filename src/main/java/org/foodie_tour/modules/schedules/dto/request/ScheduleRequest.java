package org.foodie_tour.modules.schedules.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {

    @NotNull(message = "TourID không được để trống")
    private Long tourId;

    @NotNull(message = "RouteID không được để trống")
    private Long routeId;
    private String scheduleNote;
    private String scheduleDescription;

    @NotNull(message = "Số lượng khách tối thiểu không được để trống")
    @Min(value = 1, message = "Số lượng khách tối thiểu ít nhất là 1")
    private Integer minPax;

    @Min(value = 1, message = "Số lượng khách tối đa ít nhất là 1")
    private Integer maxPax;
    @Schema(type = "string", example = "2026-01-01T08:00:00", description = "Thời điểm khởi hành (yyyy-MM-dd'T'HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureAt;
    private ScheduleStatus scheduleStatus;
    private Boolean isTemplate;
}
