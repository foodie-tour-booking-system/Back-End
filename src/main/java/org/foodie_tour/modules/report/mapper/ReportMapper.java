package org.foodie_tour.modules.report.mapper;

import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.report.dto.BookingReportResponse;
import org.foodie_tour.modules.report.dto.TransactionReportResponse;
import org.foodie_tour.modules.transaction.entity.Transactions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReportMapper {
    @Mapping(
            target = "totalCustomers",
            expression = "java(booking.getAdultCount() + booking.getChildrenCount())"
    )
    BookingReportResponse toBookingReportResponse(Booking booking);

    TransactionReportResponse toTransactionReportResponse(Transactions transactions);
}

