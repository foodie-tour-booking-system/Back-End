package org.foodie_tour.modules.tours.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.enums.DishType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = { "tour", "images" })
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

    @Column(name = "is_primary")
    private Boolean isPrimary;

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

    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;
}
