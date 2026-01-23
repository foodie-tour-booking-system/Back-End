package org.foodie_tour.modules.routes.dto.request;

import lombok.Data;

@Data
public class RouteDetailRequest {
    private String locationName;
    private int locationOrder;
    private int durationAtLocation;
}
