package org.foodie_tour.modules.customer.repository;

import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.customer.entity.CustomerBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerBookingRepository extends JpaRepository<CustomerBooking, Long> {
    Optional<CustomerBooking> findByBooking(Booking booking);
}
