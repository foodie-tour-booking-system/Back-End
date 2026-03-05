package org.foodie_tour.modules.feedback.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.customer.entity.Customer;
import org.foodie_tour.modules.feedback.enums.FeedbackStatus;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.tours.entity.Tour;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    Long feedbackId;

    @Column(name = "title")
    String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "rating")
    Integer rating;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    List<Image> images;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    FeedbackStatus status;
}
