package org.foodie_tour.modules.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.RelocateRequestStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@ToString()
public class RelocateBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relocate_request_id")
    long relocateRequestId;

    @Column(name = "booking_code")
    String bookingCode;

    @Column(name = "schedule_id")
    long scheduleId;

    @Column(name = "departure_at")
    LocalDateTime departureAt;

    @Column(name = "relocate_request_status")
    @Enumerated(EnumType.STRING)
    RelocateRequestStatus relocateRequestStatus;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
