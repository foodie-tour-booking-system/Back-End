package org.foodie_tour.modules.booking.repository;

import org.foodie_tour.modules.booking.entity.RelocateBooking;
import org.foodie_tour.modules.booking.enums.RelocateRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelocateBookingRepository extends JpaRepository<RelocateBooking, Long> {
    @Query(value = "SELECT r FROM RelocateBooking r WHERE r.relocateRequestStatus = :status")
    List<RelocateBooking> getAllByStatus(@Param(value = "status") RelocateRequestStatus status);
}
