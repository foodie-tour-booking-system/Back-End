package org.foodie_tour.modules.auth.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.auth.dto.request.CustomRolePermissionRequest;
import org.foodie_tour.modules.auth.dto.request.RoleCreateRequest;
import org.foodie_tour.modules.auth.dto.response.RoleResponse;
import org.foodie_tour.modules.auth.entity.Permission;
import org.foodie_tour.modules.auth.entity.Role;
import org.foodie_tour.modules.auth.enums.RoleStatus;
import org.foodie_tour.modules.auth.mapper.RoleMapper;
import org.foodie_tour.modules.auth.repository.PermissionRepository;
import org.foodie_tour.modules.auth.repository.RoleRepository;
import org.foodie_tour.modules.auth.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Transactional
    public RoleResponse create(RoleCreateRequest request) {
        roleRepository.findByName(request.getName()).ifPresent(existing -> {
            throw new DuplicateResourceException("Vai trò '" + request.getName() + "' đã tồn tại");
        });

        Role role = roleMapper.toEntity(request);
        role.setStatus(RoleStatus.ACTIVE);
        roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getAll(RoleStatus status) {
        if (status == null) {
            return roleRepository.findAll()
                    .stream()
                    .map(roleMapper::toResponse)
                    .toList();
        } else {
            return roleRepository.findByStatus(status)
                    .stream()
                    .map(roleMapper::toResponse)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public RoleResponse getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò không tồn tại"));
        return roleMapper.toResponse(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò không tồn tại"));
        role.setStatus(RoleStatus.DELETED);
        roleRepository.save(role);
    }

    @Transactional
    public RoleResponse customRolePermission(Long roleId, CustomRolePermissionRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò không tồn tại"));

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());

        if (permissions.size() != request.getPermissionIds().size()) {
            throw new ResourceNotFoundException("Một số quyền không tồn tại");
        }

        role.setPermissions(permissions);
        roleRepository.save(role);
        return roleMapper.toResponse(role);
    }
}
