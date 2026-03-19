package org.foodie_tour.modules.routes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.routes.enums.RouteDetailStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteDetailResponse {
    private Long routeDetailId;
    private String locationName;
    private int locationOrder;
    private int durationAtLocation;
    private RouteDetailStatus routeDetailStatus;
    private List<String> imageUrls;
}
