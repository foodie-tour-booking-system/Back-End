package org.foodie_tour.modules.tours.service.Impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.mapper.TourMapper;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.foodie_tour.modules.tours.service.TourService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    @Override
    @Transactional
    public TourResponse createTour(TourRequest tourRequest) {
        if(tourRepository.existsByTourName(tourRequest.getTourName())) {
            throw new DuplicateResourceException("Tên tour đã tồn tại");
        }

        if (tourRequest.getBasePriceChild() > tourRequest.getBasePriceAdult()) {
            throw new InvalidateDataException("Tour trẻ em phải nhỏ hơn tour người lớn");
        }

        Tour tour = tourMapper.toEntity(tourRequest);
        tour = tourRepository.save(tour);
        return tourMapper.toResponse(tour);
    }
}
