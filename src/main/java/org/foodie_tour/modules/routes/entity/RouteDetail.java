package org.foodie_tour.modules.routes.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.routes.enums.RouteDetailStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "routes_details")
public class RouteDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_detail_id", nullable = false)
    private Long routeDetailId;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_order")
    private int locationOrder;

    @Column(name = "duration_at_location")
    private int durationAtLocation;

    @Column(name = "route_detail_status")
    @Enumerated(EnumType.STRING)
    private RouteDetailStatus routeDetailStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    @OneToMany(mappedBy = "routeDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;
}

