package org.foodie_tour.modules.routes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.tours.entity.Tour;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = { "tour", "schedules", "routeDetails" })
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "routes"
        , indexes = {
        @Index(name = "idx_route_name", columnList = "route_name"),
        @Index(name = "idx_tour_id", columnList = "tour_id")
}
)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "route_status")
    @Enumerated(EnumType.STRING)
    private RouteStatus routeStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    @JsonIgnore
    private Tour tour;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RouteDetail> routeDetails;
}
