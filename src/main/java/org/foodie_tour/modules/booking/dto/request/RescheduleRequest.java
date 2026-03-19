package org.foodie_tour.modules.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleRequest {
    private Long bookingId;
    private Long scheduleId;      // ← dùng scheduleId để tìm chính xác
    private LocalDateTime newTourDate; // giữ lại để backward compat
    private String reason;
}

