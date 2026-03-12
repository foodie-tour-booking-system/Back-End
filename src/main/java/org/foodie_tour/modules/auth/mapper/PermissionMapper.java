package org.foodie_tour.modules.auth.mapper;

import org.foodie_tour.modules.auth.dto.request.PermissionCreateRequest;
import org.foodie_tour.modules.auth.dto.response.PermissionResponse;
import org.foodie_tour.modules.auth.entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Permission toEntity(PermissionCreateRequest request);

    PermissionResponse toResponse(Permission permission);

    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(PermissionCreateRequest request, @MappingTarget Permission permission);
}
