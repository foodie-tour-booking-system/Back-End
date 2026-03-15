package org.foodie_tour.modules.momo.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.foodie_tour.config.MomoConfig;
import org.foodie_tour.modules.momo.dto.MomoPaymentRequest;
import org.foodie_tour.modules.momo.dto.MomoPaymentResponse;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

@Service
public class MomoService {

    private final MomoConfig momoConfig;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public MomoService(MomoConfig momoConfig) {
        this.momoConfig = momoConfig;
    }

    public MomoPaymentResponse createPayment(Long amount, String orderInfo) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String requestType = "payWithATM";
        String extraData = "";

        // 1. Tạo chuỗi raw signature THEO ĐÚNG THỨ TỰ BẢNG CHỮ CÁI
        String rawHmac = "accessKey=" + momoConfig.getAccessKey() +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + momoConfig.getNotifyUrl() +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + momoConfig.getPartnerCode() +
                "&redirectUrl=" + momoConfig.getReturnUrl() +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        // 2. Mã hóa HMAC SHA256
        String signature = hmacSHA256(rawHmac, momoConfig.getSecretKey());

        // 3. Đóng gói JSON gửi đi (Sửa lại key cho đúng V2)
        JsonObject json = new JsonObject();
        json.addProperty("partnerCode", momoConfig.getPartnerCode());
        // Không gửi accessKey trong body theo chuẩn V2
        json.addProperty("requestId", requestId);
        json.addProperty("amount", amount);
        json.addProperty("orderId", orderId);
        json.addProperty("orderInfo", orderInfo);
        json.addProperty("redirectUrl", momoConfig.getReturnUrl()); // Đổi key
        json.addProperty("ipnUrl", momoConfig.getNotifyUrl());      // Đổi key
        json.addProperty("extraData", extraData);
        json.addProperty("requestType", requestType);
        json.addProperty("signature", signature);
        json.addProperty("lang", "vi"); // Khuyên dùng

        // 4. Bắn Request bằng OkHttp
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(momoConfig.getPaymentUrl())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Lỗi gọi MoMo API: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, MomoPaymentResponse.class);
        }
    }

    private String hmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKey);
        byte[] hash = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return toHexString(hash);
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}

