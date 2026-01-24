package org.foodie_tour.modules.images.repository;

import org.foodie_tour.modules.images.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
