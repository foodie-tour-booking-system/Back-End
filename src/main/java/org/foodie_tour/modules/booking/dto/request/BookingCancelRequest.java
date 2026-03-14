package org.foodie_tour.modules.booking.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingCancelRequest {
    private String bookingCode;
    private String email;
    private String cancellationReason;
}