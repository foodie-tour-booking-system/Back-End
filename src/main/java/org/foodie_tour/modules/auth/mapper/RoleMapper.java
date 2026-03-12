package org.foodie_tour.modules.auth.mapper;

import org.foodie_tour.modules.auth.dto.request.RoleCreateRequest;
import org.foodie_tour.modules.auth.dto.response.RoleResponse;
import org.foodie_tour.modules.auth.entity.Permission;
import org.foodie_tour.modules.auth.entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleCreateRequest request);

    @Mapping(target = "permissionDtoList", expression = "java(toPermissionDtoList(role.getPermissions()))")
    RoleResponse toResponse(Role role);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateEntity(RoleCreateRequest request, @MappingTarget Role role);

    List<RoleResponse.PermissionDto> toPermissionDtoList(List<Permission> permissions);
}
