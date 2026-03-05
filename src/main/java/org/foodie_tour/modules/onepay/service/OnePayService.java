package org.foodie_tour.modules.onepay.service;

import jakarta.servlet.http.HttpServletRequest;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;

import java.util.Map;

public interface OnePayService {
    String generatePaymentUrl(long bookingId, HttpServletRequest request);
    String processPaymentResponse(Map<String, String> response);
}
