package org.foodie_tour.modules.routes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.routes.enums.RouteDetailStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDetailRequest {
    private String locationName;
    private int locationOrder;
    private int durationAtLocation;
    private RouteDetailStatus routeDetailStatus;
}
