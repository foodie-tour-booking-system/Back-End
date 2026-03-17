package org.foodie_tour.modules.feedback.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.feedback.dto.request.FeedbackRequest;
import org.foodie_tour.modules.feedback.dto.response.FeedbackResponse;
import org.foodie_tour.modules.feedback.dto.response.FeedbackSummaryResponse;
import org.foodie_tour.modules.feedback.service.FeedbackService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedbackResponse> createFeedback(
            @RequestPart("request") @Schema(implementation = FeedbackRequest.class) FeedbackRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        FeedbackResponse response = feedbackService.createFeedback(request, files);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<FeedbackSummaryResponse> getFeedback(@PathVariable("tourId") Long tourId) {
        FeedbackSummaryResponse response = feedbackService.getFeedbackSummary(tourId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable Long id,
            @RequestPart("request") @Schema(implementation = FeedbackRequest.class) FeedbackRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        FeedbackResponse response = feedbackService.updateFeedback(id, request, files);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Xóa đánh giá thành công");
    }
}
