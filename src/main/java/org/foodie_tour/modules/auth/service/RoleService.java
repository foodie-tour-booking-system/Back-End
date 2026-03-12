package org.foodie_tour.modules.auth.service;

import org.foodie_tour.modules.auth.dto.request.CustomRolePermissionRequest;
import org.foodie_tour.modules.auth.dto.request.RoleCreateRequest;
import org.foodie_tour.modules.auth.dto.response.RoleResponse;
import org.foodie_tour.modules.auth.enums.RoleStatus;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleCreateRequest request);

    List<RoleResponse> getAll(RoleStatus status);

    RoleResponse getById(Long id);

    void delete(Long id);

    RoleResponse customRolePermission(Long roleId, CustomRolePermissionRequest request);
}
