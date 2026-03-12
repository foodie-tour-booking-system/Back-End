package org.foodie_tour.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.auth.dto.request.PermissionCreateRequest;
import org.foodie_tour.modules.auth.dto.response.PermissionResponse;
import org.foodie_tour.modules.auth.enums.PermissionStatus;
import org.foodie_tour.modules.auth.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> create(@Valid @RequestBody PermissionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAll(@RequestParam(value = "status", required = false) PermissionStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getAll(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Đã xoá");
    }
}
