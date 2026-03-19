package org.foodie_tour.modules.tracking;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.booking.repository.BookingRepository;
import org.foodie_tour.modules.tracking.dto.TrackingResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final BookingRepository bookingRepository;

    public TrackingResponse trackBooking(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode)
                .map(booking -> {
                    TrackingResponse response = new TrackingResponse();
                    response.setBookingCode(booking.getBookingCode());

                    if (booking.getTour() != null) {
                        response.setTourName(booking.getTour().getTourName());
                        response.setTourDescription(booking.getTour().getTourDescription());
                    }

                    if (booking.getSchedule() != null) {
                        response.setDepartureTime(booking.getSchedule().getDepartureAt());
                        if (booking.getSchedule().getRoute() != null) {
                            response.setRouteDescription(booking.getSchedule().getRoute().getRouteName());
                            
                            // Map itinerary from route details
                            if (booking.getSchedule().getRoute().getRouteDetails() != null) {
                                java.util.List<TrackingResponse.TrackingStep> steps = booking.getSchedule().getRoute().getRouteDetails().stream()
                                        .sorted(java.util.Comparator.comparingInt(org.foodie_tour.modules.routes.entity.RouteDetail::getLocationOrder))
                                        .map(detail -> {
                                            TrackingResponse.TrackingStep step = new TrackingResponse.TrackingStep();
                                            step.setName(detail.getLocationName());
                                            step.setTime("Stop #" + detail.getLocationOrder());
                                            step.setCompleted(detail.getLocationOrder() <= 1); 
                                            step.setType("STOP");
                                            return step;
                                        })
                                        .collect(java.util.stream.Collectors.toList());
                                response.setItinerary(steps);
                            }
                        }
                    }

                    response.setPickupLocation(booking.getPickupLocation());
                    if (booking.getBookingStatus() != null) {
                        response.setBookingStatus(booking.getBookingStatus().name());
                    }
                    response.setAdultCount(booking.getAdultCount());
                    response.setChildrenCount(booking.getChildrenCount());
                    response.setTotalPrice(booking.getTotalPrice());
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Sorry, we couldn't find any tour with this booking code."));
    }
}
