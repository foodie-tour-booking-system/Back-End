package org.foodie_tour.modules.booking.mapper;

import org.foodie_tour.modules.booking.dto.response.RelocateBookingResponse;
import org.foodie_tour.modules.booking.entity.RelocateBooking;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RelocateBookingMapper {
    RelocateBookingResponse toResponse(RelocateBooking booking);
}
