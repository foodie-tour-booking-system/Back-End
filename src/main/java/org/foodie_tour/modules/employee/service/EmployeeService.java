package org.foodie_tour.modules.employee.service;

import org.foodie_tour.modules.employee.dto.request.EmployeeCreateRequest;
import org.foodie_tour.modules.employee.dto.request.EmployeeUpdateRequest;
import org.foodie_tour.modules.employee.dto.request.SetPasswordRequest;
import org.foodie_tour.modules.employee.dto.response.EmployeeResponse;
import org.foodie_tour.modules.employee.enums.EmployeeStatus;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeCreateRequest request);

    List<EmployeeResponse> getAll(EmployeeStatus status);

    EmployeeResponse getById(Long id);

    EmployeeResponse update(long id, EmployeeUpdateRequest request);

    EmployeeResponse changeStatus(Long id, EmployeeStatus status);

    EmployeeResponse updateRole(Long id, Long roleId);

    void updatePassword(SetPasswordRequest request, String accessToken);
}
