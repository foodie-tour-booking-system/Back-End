
package org.foodie_tour.modules.booking.repository;

import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>, JpaSpecificationExecutor<Booking> {
    @Query(value = "SELECT b.totalPrice FROM Booking b WHERE b.bookingId = :bookingId")
    Optional<Long> getPriceByBookingId(@Param(value = "bookingId") long bookingId);

    Optional<Booking> findByBookingCode(String bookingCode);
    Optional<Booking> findByEmail(String email);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.departureTime >= :from AND b.departureTime < :to AND b.bookingStatus = :status")
    Long findTotalBetweenAndStatus(@Param(value = "from") LocalDateTime from,
                                 @Param(value = "to") LocalDateTime to,
                                 @Param(value = "status") BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalPrice), 0L) FROM Booking b WHERE b.departureTime >= :from AND b.departureTime < :to AND b.bookingStatus = :status")
    Long countTotalRevenueBetween(@Param(value = "from") LocalDateTime from,
                                  @Param(value = "to") LocalDateTime to,
                                  @Param(value = "status") BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.adultCount + b.childrenCount), 0L) FROM Booking b WHERE b.departureTime >= :from AND b.departureTime < :to AND b.bookingStatus = :status")
    Long countTotalCustomerBetween(@Param(value = "from") LocalDateTime from,
                                  @Param(value = "to") LocalDateTime to,
                                  @Param(value = "status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.departureTime >= :from AND b.departureTime < :to AND b.bookingStatus = :status ORDER BY b.departureTime DESC")
    List<Booking> findAllBetweenAndStatus(@Param(value = "from") LocalDateTime from,
                                            @Param(value = "to") LocalDateTime to,
                                            @Param(value = "status") BookingStatus status,
                                            Pageable pageable);
}