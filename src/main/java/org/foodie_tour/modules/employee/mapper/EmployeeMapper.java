package org.foodie_tour.modules.employee.mapper;

import org.foodie_tour.modules.employee.dto.request.EmployeeCreateRequest;
import org.foodie_tour.modules.employee.dto.response.EmployeeResponse;
import org.foodie_tour.modules.employee.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    @Mapping(target = "role", ignore = true)
    Employee toEntity(EmployeeCreateRequest request);

    @Mapping(source = "roleName", target = "role.name")
    Employee toEntity(EmployeeResponse employeeResponse);

    @Mapping(source = "role.name", target = "roleName")
    EmployeeResponse toResponse(Employee employee);
}
