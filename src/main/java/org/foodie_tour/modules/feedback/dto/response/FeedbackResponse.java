package org.foodie_tour.modules.feedback.dto.response;

import lombok.Builder;
import lombok.Data;
import org.foodie_tour.modules.feedback.enums.FeedbackStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {

    private Long feedbackId;
    private Long tourId;
    private String customerName;
    private String description;
    private Integer rating;
    private FeedbackStatus feedbackStatus;
    private LocalDateTime createdAt;
    private LocalDateTime departureAt;
}
