package org.foodie_tour.modules.vnpay.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayResultResponse {
    String status; // SUCCESS, FAILED
    String message;
    BookingResponse booking;
}
