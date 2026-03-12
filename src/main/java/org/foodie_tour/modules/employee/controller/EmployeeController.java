package org.foodie_tour.modules.employee.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.auth.dto.request.VerifyPasswordRequest;
import org.foodie_tour.modules.auth.enums.TokenScope;
import org.foodie_tour.modules.auth.service.AuthService;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.modules.employee.dto.request.EmployeeCreateRequest;
import org.foodie_tour.modules.employee.dto.request.EmployeeUpdateRequest;
import org.foodie_tour.modules.employee.dto.request.SetPasswordRequest;
import org.foodie_tour.modules.employee.dto.response.EmployeeResponse;
import org.foodie_tour.modules.employee.enums.EmployeeStatus;
import org.foodie_tour.modules.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {
    EmployeeService employeeService;
    AuthService authService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll(@RequestParam(value = "status", required = false) EmployeeStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAll(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody EmployeeUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmployeeResponse> changeStatus(@PathVariable Long id,
                                                         @RequestParam(value = "status") EmployeeStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.changeStatus(id, status));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<EmployeeResponse> updateRole(@PathVariable Long id,
                                                       @RequestParam(value = "roleId") Long roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateRole(id, roleId));
    }

    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyCurrentPassword(@Valid @RequestBody VerifyPasswordRequest request,
                                                        @RequestParam(value = "scope") TokenScope scope,
                                                        @RequestParam(value = "ttl") long timeToLive) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyCurrentPassword(request, scope, timeToLive));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody SetPasswordRequest request,
                                                 @RequestHeader("Access-Token") String accessToken) {
        if (!authService.verifyTokenByScope(accessToken, TokenScope.CHANGE_PASSWORD)) {
            throw new InvalidateDataException("Token không hợp lệ hoặc đã hết hạn");
        }

        employeeService.updatePassword(request, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("Cập nhật mật khẩu thành công");
    }
}
