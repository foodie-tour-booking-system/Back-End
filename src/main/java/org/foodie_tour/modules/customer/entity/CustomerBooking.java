package org.foodie_tour.modules.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foodie_tour.modules.booking.entity.Booking;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_bookings")
public class CustomerBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerBookingId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Boolean isMain;
}
