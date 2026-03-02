package org.foodie_tour.modules.transaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.enums.PaymentMethod;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.foodie_tour.modules.transaction.enums.TransactionStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    Long transactionId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "booking_id")
    Booking booking;

    @Column(name = "gateway_transaction_id")
    Long gatewayTransactionId;

    @Column(name = "amount")
    Long amount;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "cash_flow")
    @Enumerated(EnumType.STRING)
    CashFlow cashFlow;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    TransactionStatus status;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;
}