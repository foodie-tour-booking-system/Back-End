package org.foodie_tour.modules.momo.controller;

import org.foodie_tour.modules.momo.dto.MomoPaymentResponse;
import org.foodie_tour.modules.momo.service.MomoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/momo")
public class MomoController {

    private final MomoService momoService;

    public MomoController(MomoService momoService) {
        this.momoService = momoService;
    }

    // 1. API Tạo Link Thanh Toán
    @PostMapping("/create-payment")
    public ResponseEntity<MomoPaymentResponse> createPayment(@RequestParam Long amount, @RequestParam String orderInfo) {
        try {
            MomoPaymentResponse response = momoService.createPayment(amount, orderInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 2. Return URL: Nơi MoMo chuyển hướng người dùng về sau khi thanh toán trên app
    @GetMapping("/result")
    public ResponseEntity<String> paymentResult(@RequestParam Map<String, String> params) {
        // Lấy các thông số quan trọng
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");
        String signature = params.get("signature");

        System.out.println("MoMo Return URL - OrderId: " + orderId + ", ResultCode: " + resultCode);

        // TODO: Validate Signature tại đây
        // String rawHmac = "..."; // Ghép chuỗi theo đúng tài liệu MoMo V2
        // String calculatedSignature = hmacSHA256(rawHmac, secretKey);
        // if (!calculatedSignature.equals(signature)) return lỗi bảo mật

        if ("0".equals(resultCode)) {
            return ResponseEntity.ok("Thanh toán thành công cho đơn hàng: " + orderId);
        } else {
            return ResponseEntity.badRequest().body("Thanh toán thất bại hoặc bị hủy.");
        }
    }

    // 3. Notify URL (IPN): Máy chủ MoMo gọi ngầm về hệ thống của bạn (Webhook)
    @PostMapping("/notify")
    public ResponseEntity<Void> paymentNotify(@RequestBody Map<String, Object> requestBody) {
        // Lấy dữ liệu
        String resultCode = String.valueOf(requestBody.get("resultCode"));
        String orderId = String.valueOf(requestBody.get("orderId"));
        String signature = String.valueOf(requestBody.get("signature"));

        System.out.println("MoMo IPN - OrderId: " + orderId + ", ResultCode: " + resultCode);

        // TODO: Validate Signature tại đây (Rất quan trọng để chống Fake Request)

        if ("0".equals(resultCode)) {
            // TODO: Cập nhật trạng thái đơn hàng orderId thành "Đã thanh toán" trong Database
        }

        // Theo tài liệu MoMo V2, server nên trả về HTTP 204 No Content cho IPN request
        return ResponseEntity.noContent().build();
    }
}