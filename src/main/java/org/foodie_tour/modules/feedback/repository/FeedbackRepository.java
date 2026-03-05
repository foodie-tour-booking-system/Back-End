package org.foodie_tour.modules.feedback.repository;

import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.feedback.entity.Feedback;
import org.foodie_tour.modules.feedback.enums.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTour_TourIdAndStatusOrderByCreatedAtDesc(Long tourId, FeedbackStatus status);

    @Query("SELECT COALESCE(AVG(f.rating), 0) FROM Feedback f WHERE f.tour.tourId = :tourId AND f.status = 'ACTIVE'")
    Double getAverageRatingByTourId(@Param("tourId") Long tourId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.tour.tourId = :tourId AND f.status = 'ACTIVE'")
    Long countByTourId(@Param("tourId") Long tourId);
}
