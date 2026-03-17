package org.foodie_tour.modules.schedules.specifications;

import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.springframework.data.jpa.domain.Specification;

public class ScheduleSpecification {

    public static Specification<Schedule> hasTourId(Long tourId) {
        return (root, query, cb) -> tourId == null ? null :
                cb.equal(root.get("tour").get("tourId"), tourId);
    }

    public static Specification<Schedule> hasRouteId(Long routeId) {
        return (root, query, cb) -> routeId == null ? null :
                cb.equal(root.get("route").get("routeId"), routeId);
    }

    public static Specification<Schedule> hasStatus(ScheduleStatus status) {
        return (root, query, cb) -> status == null ? null :
                cb.equal(root.get("scheduleStatus"), status);
    }
}
