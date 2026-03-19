package org.foodie_tour.modules.booking.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.PaymentMethod;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    long tourId;
    long scheduleId;
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    String customerName;
    String email;
    String phone;
    org.foodie_tour.modules.tours.enums.TourType tourType;
    int adultCount;
    int childrenCount;
    String pickupLocation;
    String customerNote;
    boolean deposit;
    PaymentMethod paymentMethod;

}