package org.foodie_tour.modules.booking.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.booking.enums.PaymentMethod;
import org.foodie_tour.modules.booking.enums.RefundStatus;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.transaction.entity.Transactions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "schedule", "bookingLogs", "bookingTransactions" })
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_code", columnList = "booking_code"),
        @Index(name = "idx_booking_status", columnList = "booking_status")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    Tour tour;

    @Column(name = "booking_code")
    String bookingCode;

    @Column(name = "customer_name")
    String customerName;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "total_price")
    Long totalPrice;

    @Column(name = "adult_count")
    int adultCount;

    @Column(name = "children_count")
    int childrenCount;

    @Column(name = "pickup_location")
    String pickupLocation;

    @Column(name = "customer_note", columnDefinition = "TEXT")
    String customerNote;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    String cancellationReason;

    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;

    @Column(name = "booking_status")
    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    @Column(name = "refund_status")
    @Enumerated(EnumType.STRING)
    RefundStatus refundStatus;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(name = "is_deposit")
    Boolean deposit;

    @Column(name = "amount_paid")
    Long amountPaid;

    @Column(name = "remaining_amount")
    Long remainingAmount;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<BookingLog> bookingLogs = new ArrayList<>();

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Transactions> bookingTransactions = new ArrayList<>();

}