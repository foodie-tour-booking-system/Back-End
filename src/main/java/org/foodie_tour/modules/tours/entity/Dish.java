package org.foodie_tour.modules.tours.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.enums.DishType;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "dish_image_url")
    private String dishImageUrl;

    @Column(name = "dish_description")
    private String dishDescription;

    @Column(name = "dish_type")
    @Enumerated(EnumType.STRING)
    private DishType dishType;

    @Column(name = "dish_status")
    @Enumerated(EnumType.STRING)
    private DishStatus dishStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
}
