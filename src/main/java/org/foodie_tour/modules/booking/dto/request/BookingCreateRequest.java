package org.foodie_tour.modules.booking.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    long scheduleId;

    String email;

    String phone;

    LocalDateTime dateTime;

    int adultCount;

    int childrenCount;

    String pickupLocation;

    String customerNote;

    PaymentMethod paymentMethod;

}