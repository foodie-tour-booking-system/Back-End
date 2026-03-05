package org.foodie_tour.modules.feedback.mapper;

import org.foodie_tour.modules.feedback.dto.request.FeedbackRequest;
import org.foodie_tour.modules.feedback.dto.response.FeedbackResponse;
import org.foodie_tour.modules.feedback.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FeedbackMapper {

        @Mapping(target = "customer", ignore = true)
        @Mapping(target = "tour", ignore = true)
        @Mapping(target = "schedule", ignore = true)
        @Mapping(target = "images", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        Feedback toEntity(FeedbackRequest request);

        @Mapping(target = "customerName", source = "customer.customerName")
        @Mapping(target = "tourId", source = "tour.tourId")
        @Mapping(target = "departureAt", source = "schedule.departureAt")
        @Mapping(target = "feedbackStatus", source = "status")
        FeedbackResponse toResponse(Feedback feedback);

        @Mapping(target = "feedbackId", ignore = true)
        @Mapping(target = "customer", ignore = true)
        @Mapping(target = "tour", ignore = true)
        @Mapping(target = "schedule", ignore = true)
        @Mapping(target = "images", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        void updatedEntity(FeedbackRequest request, @MappingTarget Feedback feedback);
}
