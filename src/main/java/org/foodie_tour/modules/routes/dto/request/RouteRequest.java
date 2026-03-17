package org.foodie_tour.modules.routes.dto.request;

import lombok.Data;
import org.foodie_tour.modules.routes.enums.RouteStatus;

import java.util.List;

@Data
public class RouteRequest {
    private String routeName;
    private RouteStatus routeStatus;
    private Long tourId;
    private List<RouteDetailRequest> routeDetails;
}
