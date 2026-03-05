package org.foodie_tour.modules.feedback.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedbackSummaryResponse {
    private Double averageRating;
    private Long totalReviews;
    private List<FeedbackResponse> reviews;
}
