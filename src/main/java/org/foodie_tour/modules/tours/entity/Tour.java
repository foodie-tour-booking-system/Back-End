package org.foodie_tour.modules.tours.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.images.entity.TourImage;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.foodie_tour.modules.tours.enums.TourType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tours", indexes = {
        @Index(name = "idx_tour_status_price", columnList = "tour_status, base_price_adult"),
        @Index(name = "idx_tour_name_lower", columnList = "tour_name")
})
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id", nullable = false)
    private Long tourId;

    @Column(name = "tour_name")
    private String tourName;

    @Column(name = "tour_description", columnDefinition = "TEXT")
    private String tourDescription;

    @Column(name = "duration")
    private int duration;

    @Column(name = "base_price_adult")
    private Long basePriceAdult;

    @Column(name = "base_price_child")
    private Long basePriceChild;

    @Column(name = "tour_type")
    @Enumerated(EnumType.STRING)
    private TourType tourType;

    @Column(name = "tour_status")
    @Enumerated(EnumType.STRING)
    private TourStatus tourStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Dish> dishes;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Route> routes;

    @OneToMany(mappedBy = "tour")
    private List<TourImage> tourImages;
}

