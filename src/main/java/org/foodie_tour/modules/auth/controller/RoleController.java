package org.foodie_tour.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.auth.dto.request.CustomRolePermissionRequest;
import org.foodie_tour.modules.auth.dto.request.RoleCreateRequest;
import org.foodie_tour.modules.auth.dto.response.RoleResponse;
import org.foodie_tour.modules.auth.enums.RoleStatus;
import org.foodie_tour.modules.auth.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    public ResponseEntity<List<RoleResponse>> getAll(@RequestParam(value = "status", required = false) RoleStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getAll(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Đã xoá");
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('MAP_ROLE_PERMISSION')")
    public ResponseEntity<RoleResponse> customRolePermission(@PathVariable Long id,
                                                             @Valid @RequestBody CustomRolePermissionRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.customRolePermission(id, request));
    }
}
