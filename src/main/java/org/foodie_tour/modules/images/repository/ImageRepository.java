package org.foodie_tour.modules.images.repository;

import org.foodie_tour.modules.images.entity.Image;
import org.foodie_tour.modules.images.enums.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findImageByImageStatus(ImageStatus imageStatus);
}
