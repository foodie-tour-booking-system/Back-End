package org.foodie_tour.modules.booking.repository;

import org.foodie_tour.modules.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>, JpaSpecificationExecutor<Booking> {
    @Query(value = "SELECT b.totalPrice FROM Booking b WHERE b.bookingId = :bookingId")
    Optional<Long> getPriceByBookingId(@Param(value = "bookingId") long bookingId);

    Optional<Booking> findByBookingCode(String bookingCode);
    Optional<Booking> findByEmail(String email);
    Optional<Booking> findByBookingCodeAndEmail(String bookingCode, String email);
}