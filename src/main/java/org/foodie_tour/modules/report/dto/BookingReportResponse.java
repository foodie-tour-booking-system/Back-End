package org.foodie_tour.modules.report.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.BookingStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingReportResponse {
    String bookingCode;
    Long totalPrice;
    BookingStatus bookingStatus;
    LocalDateTime departureTime;
    Integer totalCustomers;
}
