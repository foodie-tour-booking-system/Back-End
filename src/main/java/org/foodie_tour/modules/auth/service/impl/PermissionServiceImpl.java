package org.foodie_tour.modules.auth.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.auth.dto.request.PermissionCreateRequest;
import org.foodie_tour.modules.auth.dto.response.PermissionResponse;
import org.foodie_tour.modules.auth.entity.Permission;
import org.foodie_tour.modules.auth.enums.PermissionStatus;
import org.foodie_tour.modules.auth.mapper.PermissionMapper;
import org.foodie_tour.modules.auth.repository.PermissionRepository;
import org.foodie_tour.modules.auth.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse create(PermissionCreateRequest request) {
        permissionRepository.findByName(request.getName()).ifPresent(existing -> {
            throw new DuplicateResourceException("Quyền '" + request.getName() + "' đã tồn tại");
        });

        Permission permission = permissionMapper.toEntity(request);
        permission.setStatus(PermissionStatus.ACTIVE);
        permissionRepository.save(permission);
        return permissionMapper.toResponse(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> getAll(PermissionStatus status) {
        if (status == null) {
            return permissionRepository.findAll()
                    .stream()
                    .map(permissionMapper::toResponse)
                    .toList();
        } else {
            return permissionRepository.findByStatus(status)
                    .stream()
                    .map(permissionMapper::toResponse)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public PermissionResponse getById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quyền không tồn tại"));
        return permissionMapper.toResponse(permission);
    }

    @Transactional
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quyền không tồn tại"));
        permission.setStatus(PermissionStatus.DELETED);
        permissionRepository.save(permission);
    }
}
