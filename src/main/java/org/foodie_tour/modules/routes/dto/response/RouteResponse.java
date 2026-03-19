package org.foodie_tour.modules.routes.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RouteResponse {
    private Long routeId;
    private Long tourId;
    private String routeName;
    private List<RouteDetailResponse> routeDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

