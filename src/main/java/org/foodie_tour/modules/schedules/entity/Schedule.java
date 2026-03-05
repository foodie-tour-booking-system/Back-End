package org.foodie_tour.modules.schedules.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.foodie_tour.modules.tours.entity.Tour;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules", indexes = {
        @Index(name = "idx_schedule_tour_status", columnList = "tour_id, status"),
        @Index(name = "idx_schedule_departure", columnList = "departure_at")
})
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "schedule_note", nullable = true)
    private String scheduleNote;

    @Column(name = "schedule_description")
    private String scheduleDescription;

    @Column(name = "max_pax")
    private int maxPax;

    @Column(name = "min_pax")
    private int minPax;

    @Column(name = "schedule_status")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    @Column(name = "departure_at")
    private LocalDateTime departureAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;
}
