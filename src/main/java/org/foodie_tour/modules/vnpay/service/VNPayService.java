package org.foodie_tour.modules.vnpay.service;

import jakarta.servlet.http.HttpServletRequest;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;

import java.util.Map;

public interface VNPayService {
    String generatePaymentUrl(PaymentRequest request, HttpServletRequest servletRequest);
    Map<String, String> processIPNFromVnpay(Map<String, String> response);
    String processPaymentResponse(Map<String, String> response);
}
