package org.foodie_tour.modules.booking.repository;

import org.foodie_tour.modules.booking.entity.BookingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingLogRepository extends JpaRepository<BookingLog,Long> {

    @Query(value = "SELECT logs FROM BookingLog logs WHERE logs.booking.bookingCode = :bookingCode")
    List<BookingLog> getLogsByBookingCode(@Param(value = "bookingCode") String bookingCode);
}