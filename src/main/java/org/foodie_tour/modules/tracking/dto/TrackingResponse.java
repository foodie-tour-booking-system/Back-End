package org.foodie_tour.modules.tracking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrackingResponse {
    private String bookingCode;
    private String tourName;
    private String tourDescription;
    private LocalDateTime departureTime;
    private String pickupLocation;
    private String bookingStatus;
    private int adultCount;
    private int childrenCount;
    private Long totalPrice;
    private String routeDescription;
    private java.util.List<TrackingStep> itinerary;

    @lombok.Data
    public static class TrackingStep {
        private String name;
        private String time;
        @com.fasterxml.jackson.annotation.JsonProperty("isCompleted")
        private boolean isCompleted;
        private String type;
    }
}
