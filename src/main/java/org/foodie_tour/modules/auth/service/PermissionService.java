package org.foodie_tour.modules.auth.service;

import org.foodie_tour.modules.auth.dto.request.PermissionCreateRequest;
import org.foodie_tour.modules.auth.dto.response.PermissionResponse;
import org.foodie_tour.modules.auth.enums.PermissionStatus;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionCreateRequest request);

    List<PermissionResponse> getAll(PermissionStatus status);

    PermissionResponse getById(Long id);

    void delete(Long id);
}
