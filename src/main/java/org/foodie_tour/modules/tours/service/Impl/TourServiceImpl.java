package org.foodie_tour.modules.tours.service.Impl;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.rag.RagUtils;
import org.foodie_tour.modules.tours.dto.request.TourRequest;
import org.foodie_tour.modules.tours.dto.response.TourResponse;
import org.foodie_tour.modules.tours.entity.Tour;
import org.foodie_tour.modules.tours.enums.TourStatus;
import org.foodie_tour.modules.tours.mapper.TourMapper;
import org.foodie_tour.modules.tours.repository.TourRepository;
import org.foodie_tour.modules.tours.service.TourService;
import org.foodie_tour.modules.tours.specification.TourSpecifications;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final RagUtils ragUtils;
    private final VectorStore vectorStore;

    @Override
    @Transactional
    public TourResponse createTour(TourRequest tourRequest) {
        if(tourRepository.existsByTourName(tourRequest.getTourName())) {
            throw new DuplicateResourceException("Tên tour đã tồn tại");
        }

        if (tourRequest.getGroupPriceChild() > tourRequest.getGroupPriceAdult()) {
            throw new InvalidateDataException("Giá tour trẻ em (Group) phải nhỏ hơn giá tour người lớn");
        }

        if (tourRequest.getPrivatePriceChild() > tourRequest.getPrivatePriceAdult()) {
            throw new InvalidateDataException("Giá tour trẻ em (Private) phải nhỏ hơn giá tour người lớn");
        }

        Tour tour = tourMapper.toEntity(tourRequest);
        tour = tourRepository.save(tour);

        ragUtils.updateVectorTour(tour);

        return tourMapper.toResponse(tour);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourResponse> getAllTours(TourStatus tourStatus) {
        List<Tour> tours;
        if (tourStatus != null) {
            tours = tourRepository.findByTourStatus(tourStatus);
        } else {
            tours = tourRepository.findAll();
        }

        return tours.stream()
                .map(tourMapper::toResponse)
                .toList();
    }

    @Override
    public TourResponse getTourById(Long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        return tourMapper.toResponse(tour);
    }

    @Override
    public TourResponse updateTour(Long tourId, TourRequest tourRequest) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));

        if (tourRequest.getGroupPriceChild() > tourRequest.getGroupPriceAdult()) {
            throw new InvalidateDataException("Giá tour trẻ em (Group) phải nhỏ hơn giá tour người lớn");
        }

        if (tourRequest.getPrivatePriceChild() > tourRequest.getPrivatePriceAdult()) {
            throw new InvalidateDataException("Giá tour trẻ em (Private) phải nhỏ hơn giá tour người lớn");
        }

        tourMapper.updateEntity(tourRequest, tour);
        tour.setUpdatedAt(LocalDateTime.now());
        tour = tourRepository.save(tour);

        ragUtils.updateVectorTour(tour);

        return tourMapper.toResponse(tour);
    }

    @Override
    public void deleteTour(Long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour không tồn tại"));
        tour.setTourStatus(TourStatus.DELETED);
        tourRepository.save(tour);

        vectorStore.delete(List.of(tour.getVectorId()));

    }

    @Override
    public Page<TourResponse> searchTours(String name, TourStatus status, Long minPrice, Long maxPrice, Pageable pageable) {
        Specification<Tour> spec = Specification.where(TourSpecifications.hasName(name))
                .and(TourSpecifications.hasStatus(status))
                .and(TourSpecifications.priceBetween(minPrice, maxPrice));
        return tourRepository.findAll(spec, pageable)
                .map(tourMapper::toResponse);
    }
}
