package org.foodie_tour.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
public class OnePayConfig {

    @Value("${onepay.merchant-id}")
    private String merchantId;

    @Value("${onepay.access-code}")
    private String accessCode;

    @Value("${onepay.hash-secret}")
    private String hashSecret;

    @Value("${onepay.payment-url}")
    private String payUrl;

    @Value("${onepay.return-url}")
    private String returnUrl;
}
