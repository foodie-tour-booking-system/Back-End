package org.foodie_tour.modules.feedback.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackRequest {
    private String bookingCode;
    private String email;
    private String title;

    @Size(min = 10, max = 100)
    private String description;

    @Min(1)
    @Max(5)
    private Integer rating;
    private LocalDateTime createdAt;
}
