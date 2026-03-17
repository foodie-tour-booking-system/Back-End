package org.foodie_tour.modules.vnpay.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;
import org.foodie_tour.modules.vnpay.service.VNPayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/payment/vnpay")
public class VNPayController {
    VNPayService vnPayService;

    @PostMapping("/generate-payment-url")
    public ResponseEntity<String> generatePaymentUrl(@RequestBody PaymentRequest request, HttpServletRequest servletRequest) {
        var result = vnPayService.generatePaymentUrl(request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/process-ipn")
    public ResponseEntity<Map<String, String>> processIPNFromVnpay(@RequestBody Map<String, String> response) {
        var result = vnPayService.processIPNFromVnpay(response);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/status")
    public RedirectView processPaymentResponse(@RequestParam Map<String, String> queryParams) {
        RedirectView redirectView = new RedirectView();
        String url = vnPayService.processPaymentResponse(queryParams);
        redirectView.setUrl(url);
        return redirectView;
    }
}
