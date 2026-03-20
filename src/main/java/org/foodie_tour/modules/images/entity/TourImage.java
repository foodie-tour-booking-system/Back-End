package org.foodie_tour.modules.images.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.foodie_tour.modules.images.enums.TourImageStatus;
import org.foodie_tour.modules.tours.entity.Tour;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = { "image", "tour" })
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tour_images")
public class TourImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_image_id")
    private Long tourImageId;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "tour_image_status")
    @Enumerated(EnumType.STRING)
    private TourImageStatus tourImageStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;
}