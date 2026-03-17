package org.foodie_tour.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.auth.dto.request.IntrospectRequest;
import org.foodie_tour.modules.auth.dto.request.LoginRequest;
import org.foodie_tour.modules.auth.dto.response.IntrospectResponse;
import org.foodie_tour.modules.auth.dto.response.LoginResponse;
import org.foodie_tour.modules.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.introspect(request.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.status(HttpStatus.OK).body("Đăng xuất thành công");
    }
}
