package org.foodie_tour.common.rag;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.routes.entity.Route;
import org.foodie_tour.modules.routes.entity.RouteDetail;
import org.foodie_tour.modules.routes.enums.RouteStatus;
import org.foodie_tour.modules.routes.repository.RouteDetailRepository;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.tours.entity.Dish;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.enums.DishStatus;
import org.foodie_tour.modules.tours.enums.DishType;
import org.foodie_tour.modules.tours.repository.DishRepository;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RagQueryTools {
    RouteDetailRepository routeDetailRepository;
    DishRepository dishRepository;
    ScheduleRepository scheduleRepository;
    TourRepository tourRepository;
    VectorStore vectorStore;

    @Tool(description = """
            Tìm thông tin chi tiết route của tour bằng tour id.
            Dùng khi được hỏi về địa điểm trong tour.
            Không dùng khi được hỏi về món ăn, thời gian xuất phát.
            """)
    public List<RouteDetailQueryDTO> getRouteDetailByTourId(@ToolParam(description = "Id của tour") long tourId) {
        return routeDetailRepository.findAllByTourId(tourId).stream().map(RouteDetailQueryDTO::convert).toList();
    }

    @Tool(description = """
            Tìm thông tin chi tiết về dish của tour bằng tour id.
            Dùng khi được hỏi về món ăn trong tour.
            KHông dùng khi được hỏi về địa điểm hay thời gian.
            """)
    public List<DishQueryDTO> getDishDetailByTourId(@ToolParam(description = "Id của tour") long tourId) {
        return dishRepository.findByTourId(tourId).stream().map(DishQueryDTO::convert).toList();
    }

    @Tool(description = """
            Tìm thông tin chi tiết về schedule của tour bằng tour id.
            Dùng khi được hỏi về thời gian xuất phát hoặc số người trong tour.
            """)
    public List<ScheduleQueryDTO> getScheduleDetailByTourId(@ToolParam(description = "Id của tour") long tourId) {
        return scheduleRepository.findAllByTourId(tourId).stream().map(ScheduleQueryDTO::convert).toList();
    }

    @Tool(description = """
            Tìm các tour theo chủ đề hoặc theo ngữ nghĩa
            Có thể lọc theo món ăn, địa điểm nếu được chỉ định
            """)
    public List<TourQueryDTO> getTourByTopic(
            @ToolParam(description = "Từ khoá hoặc chủ để tìm kiếm") String query,
            @ToolParam(description = "Loại món ăn, để trống nếu không có") String dishType,
            @ToolParam(description = "Tên món ăn, để trống nếu không có") String dishName,
            @ToolParam(description = "Địa điểm, để trống nếu không có") String location
    ) {
        SearchRequest.Builder searchRequest = SearchRequest.builder()
                .query(query)
                .topK(5)
                .similarityThreshold(0.65);

        List<String> filter = new ArrayList<>();
        if (dishType != null && !dishType.isEmpty()) {
            filter.add("dishTypes in ['%s']".formatted(dishType));
        }
        if (dishName != null && !dishName.isEmpty()) {
            filter.add("dishNames in ['%s']".formatted(dishName));
        }
        if (location != null && !location.isEmpty()) {
            filter.add("locationNames in ['%s']".formatted(location));
        }

        if (!filter.isEmpty()) {
            searchRequest.filterExpression(String.join(" && ", filter));
        }

        List<Document> docs = vectorStore.similaritySearch(searchRequest.build());

        List<Long> idList = docs.stream().map(document -> (Long) document.getMetadata().get("tourId")).toList();

        return tourRepository.findAllById(idList).stream().map(TourQueryDTO::convert).toList();
    }

    public record RouteDetailQueryDTO (
            long routeDetailId,
            String locationName,
            int durationAtLocation
    ) {
        public static RouteDetailQueryDTO convert(RouteDetail routeDetail) {
            return new RouteDetailQueryDTO(
                    routeDetail.getRouteDetailId(),
                    routeDetail.getLocationName(),
                    routeDetail.getDurationAtLocation()
            );
        }
    }

    public record DishQueryDTO (
            long dishId,
            String dishName,
            boolean isPrimary,
            String description,
            DishType dishType
    ) {
        public static DishQueryDTO convert(Dish dish) {
            return new DishQueryDTO(
                    dish.getDishId(),
                    dish.getDishName(),
                    dish.getIsPrimary(),
                    dish.getDishDescription(),
                    dish.getDishType()
            );
        }
    }

    public record ScheduleQueryDTO (
            long scheduleId,
            String scheduleNote,
            String scheduleDescription,
            int minPax,
            int maxPax,
            LocalDateTime departureAt
    ) {
        public static ScheduleQueryDTO convert(Schedule schedule) {
            return new ScheduleQueryDTO(
                    schedule.getScheduleId(),
                    schedule.getScheduleNote(),
                    schedule.getScheduleDescription(),
                    schedule.getMinPax(),
                    schedule.getMaxPax(),
                    schedule.getDepartureAt()
            );
        }
    }

    public record TourQueryDTO (
            long tourId,
            String tourName,
            String tourDescription,
            int duration,
            long groupPriceAdult,
            long groupPriceChild,
            long privatePriceAdult,
            long privatePriceChild,
            List<String> dishNames,
            List<LocalDateTime> schedules,
            List<String> routeNames,
            Set<String> dishType,
            List<String> locationNames
    ) {
        public static TourQueryDTO convert(Tour tour) {
            return new TourQueryDTO(
                    tour.getTourId(),
                    tour.getTourName(),
                    tour.getTourDescription(),
                    tour.getDuration(),
                    tour.getGroupPriceAdult(),
                    tour.getGroupPriceChild(),
                    tour.getPrivatePriceAdult(),
                    tour.getPrivatePriceChild(),
                    tour.getDishes().stream().filter(dish -> dish.getDishStatus().equals(DishStatus.ACTIVE)).map(Dish::getDishName).toList(),
                    tour.getSchedules().stream().filter(schedule -> schedule.getScheduleStatus().equals(ScheduleStatus.ACTIVE)).map(Schedule::getDepartureAt).toList(),
                    tour.getRoutes().stream().filter(route -> route.getRouteStatus().equals(RouteStatus.ACTIVE)).map(Route::getRouteName).toList(),
                    tour.getDishes().stream().map(dish -> dish.getDishType().toString()).collect(Collectors.toSet()),
                    tour.getRoutes().stream().filter(route -> route.getRouteStatus().equals(RouteStatus.ACTIVE)).map(route -> route.getRouteDetails().stream().map(RouteDetail::getLocationName).toList()).flatMap(List::stream).toList()
            );
        }
    }
}
