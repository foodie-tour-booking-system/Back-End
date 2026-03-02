package org.foodie_tour.modules.booking.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.BookingStatus;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    Long bookingId;

    String bookingCode;

    Long totalPrice;

    String pickupLocation;

    BookingStatus bookingStatus;
}