package org.foodie_tour.common.rag;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.entity.RouteDetail;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.foodie_tour.modules.tours.entity.Dish;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.enums.DishType;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RagUtils {
    TourRepository tourRepository;
    VectorStore vectorStore;

    @Transactional
    public void updateVectorTour(Tour tour) {
        // Delete current vector id
        if (tour.getVectorId() != null) {
            vectorStore.delete(List.of(tour.getVectorId()));
        }

        List<String> dishNames = tour.getDishes().stream().filter(dish -> dish.getDishStatus().equals(DishStatus.ACTIVE)).map(Dish::getDishName).toList();
        Set<String> dishTypes = tour.getDishes().stream().map(dish -> dish.getDishType().toString()).collect(Collectors.toSet());
        List<String> routeName = tour.getRoutes().stream().filter(route -> route.getRouteStatus().equals(RouteStatus.ACTIVE)).map(Route::getRouteName).toList();
        List<LocalDateTime> scheduleList = tour.getSchedules().stream().filter(schedule -> schedule.getScheduleStatus().equals(ScheduleStatus.ACTIVE)).map(Schedule::getDepartureAt).toList();
        List<String> locationNames = tour.getRoutes().stream().filter(route -> route.getRouteStatus().equals(RouteStatus.ACTIVE)).map(route -> route.getRouteDetails().stream().map(RouteDetail::getLocationName).toList()).flatMap(List::stream).toList();

        String content = """
                        Id: %s
                        Name: %s
                        Description: %s
                        Duration: %s
                        Group price adult: %s
                        Group price child: %s
                        Private price adult: %s
                        Private price child: %s
                        Dish: %s
                        Dish type: %s
                        Schedule: %s
                        Route: %s
                        Locations : %s
                        """.formatted(
                tour.getTourId(),
                tour.getTourName(),
                tour.getTourDescription(),
                tour.getDuration(),
                tour.getGroupPriceAdult(),
                tour.getGroupPriceChild(),
                tour.getPrivatePriceAdult(),
                tour.getPrivatePriceChild(),
                dishNames,
                dishTypes,
                scheduleList,
                routeName,
                locationNames
        );

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("tourId", tour.getTourId());
        metadata.put("locationNames", locationNames);
        metadata.put("dishNames", dishNames);
        metadata.put("dishTypes", dishTypes);

        Document document = new Document(content, metadata);

        tour.setVectorId(document.getId());

        tourRepository.save(tour);
        vectorStore.add(List.of(document));
    }
}
