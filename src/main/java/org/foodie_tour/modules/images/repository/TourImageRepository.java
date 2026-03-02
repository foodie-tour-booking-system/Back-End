package org.foodie_tour.modules.images.repository;

import org.foodie_tour.modules.images.entity.TourImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourImageRepository extends JpaRepository<TourImage, Long> {
    @Query("SELECT ti FROM TourImage ti WHERE ti.tour.tourId = :tourId")
    List<TourImage> findByTourId(Long tourId);

    @Modifying
    @Query("UPDATE TourImage ti SET ti.isPrimary = false WHERE ti.tour.tourId = :tourId")
    void resetPrimaryStatusByTourId(@Param("tourId") Long tourId);
}