package org.foodie_tour.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MomoConfig {
    @Value("${MOMO_PARTNER_CODE}")
    private String partnerCode;

    @Value("${MOMO_ACCESS_KEY}")
    private String accessKey;

    @Value("${MOMO_SECRET_KEY}")
    private String secretKey;

    @Value("${MOMO_RETURN_URL}")
    private String returnUrl;

    @Value("${MOMO_NOTIFY_URL}")
    private String notifyUrl;

    @Value("${MOMO_PAYMENT_URL}")
    private String paymentUrl;
}

