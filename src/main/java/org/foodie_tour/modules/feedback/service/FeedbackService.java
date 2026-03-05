package org.foodie_tour.modules.feedback.service;

import org.foodie_tour.modules.feedback.dto.request.FeedbackRequest;
import org.foodie_tour.modules.feedback.dto.response.FeedbackResponse;
import org.foodie_tour.modules.feedback.dto.response.FeedbackSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FeedbackService {

    FeedbackResponse createFeedback(FeedbackRequest feedbackRequest, List<MultipartFile> files) throws IOException;
    FeedbackSummaryResponse getFeedbackSummary(Long tourId);
    FeedbackResponse updateFeedback(Long feedbackId, FeedbackRequest feedbackRequest, List<MultipartFile> files) throws IOException;
    void deleteFeedback(Long feedbackId);
}
