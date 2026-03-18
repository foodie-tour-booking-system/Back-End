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
            "/api/booking/**",
            "/api/feedback/**",
            "/api/images/**",
            "/api/mail/send",
            "/api/onepay/**",
            "/api/payment/vnpay/**",
            "/api/tour/{tourId}/images/**",
    };

    public static String[] PUBLIC_POST_ENDPOINTS = {
            "/api/auth/login",
            "/api/rag-chat",
            "/api/rag-chat/prompt",
    };

    public static String[] PUBLIC_GET_ENDPOINTS = {
            "/api/routes",
            "/api/routes/{id}",
            "/api/schedules",
            "/api/dishes",
            "/api/dishes/{id}",
            "/api/tour/{id}",
            "/api/tour",
            "/api/tour/search",
            "/api/v1/customer/**"
    };
    public static String[] PUBLIC_PUT_ENDPOINTS = {

    };

}