package org.foodie_tour.modules.booking.mapper;

import org.foodie_tour.modules.booking.dto.request.BookingCreateRequest;
import org.foodie_tour.modules.booking.dto.response.BookingResponse;
import org.foodie_tour.modules.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "isDeposit", source = "deposit")
    Booking toBooking(BookingCreateRequest bookingCreateRequest);

    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "isDeposit", source = "deposit")
    void updateBooking(BookingCreateRequest request, @org.mapstruct.MappingTarget Booking booking);

    @Mapping(target = "departureTime", source = "booking.departureTime")
    @Mapping(target = "duration", source = "booking.tour.duration")
    @Mapping(target = "tourId", source = "booking.tour.tourId")
    @Mapping(target = "deposit", source = "booking.isDeposit")
    BookingResponse toResponse(Booking booking);

}