package org.foodie_tour.modules.schedules.dto.request;

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

    @Min(value = 1, message = "Thời lượng tour phải lớn hơn 0")
    private Integer duration;

    @NotNull(message = "Số lượng khách tối thiểu không được để trống")
    @Min(value = 1, message = "Số lượng khách tối thiểu ít nhất là 1")
    private Integer minPax;

    @Min(value = 1, message = "Số lượng khách tối đa ít nhất là 1")
    private Integer maxPax;
    private LocalDateTime departureAt;
    private ScheduleStatus scheduleStatus;
}
