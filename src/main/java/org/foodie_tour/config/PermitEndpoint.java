package org.foodie_tour.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermitEndpoint {
    public static String[] PUBLIC_ENDPOINTS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/ws/**",
    };

    public static String[] PUBLIC_POST_ENDPOINTS = {
        "/api/auth/login"
    };

    public static String[] PUBLIC_GET_ENDPOINTS = {

    };
    public static String[] PUBLIC_PUT_ENDPOINTS = {

    };

}