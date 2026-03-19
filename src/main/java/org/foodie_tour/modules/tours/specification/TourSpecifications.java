package org.foodie_tour.modules.tours.specification;

import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.springframework.data.jpa.domain.Specification;

public class TourSpecifications {

    public static Specification<Tour> hasName(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("tourName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Tour> hasStatus(TourStatus status) {
        return (root, query, cb) -> status == null ? null :
                cb.equal(root.get("tourStatus"), status);
    }

    public static Specification<Tour> priceBetween(Long minPrice, Long maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice == null) return cb.greaterThanOrEqualTo(root.get("groupPriceAdult"), minPrice);
            if (minPrice == null) return cb.lessThanOrEqualTo(root.get("groupPriceAdult"), maxPrice);
            return cb.between(root.get("groupPriceAdult"), minPrice, maxPrice);
        };
    }
}
