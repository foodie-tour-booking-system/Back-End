package org.foodie_tour.modules.booking.mapper;

import org.foodie_tour.modules.booking.dto.response.BookingLogResponse;
import org.foodie_tour.modules.booking.entity.BookingLog;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingLogMapper {
    BookingLogResponse toResponse(BookingLog bookingLog);
}
