package org.foodie_tour.modules.booking.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.RelocateRequestStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelocateBookingResponse {
    long relocateRequestId;

    String bookingCode;

    RelocateRequestStatus relocateRequestStatus;

    LocalDateTime departureAt;

}
