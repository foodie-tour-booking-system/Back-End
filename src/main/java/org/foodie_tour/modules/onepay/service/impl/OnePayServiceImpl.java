package org.foodie_tour.modules.onepay.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.config.OnePayConfig;
import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.entity.BookingLog;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.booking.enums.PaymentMethod;
import org.foodie_tour.modules.booking.repository.BookingLogRepository;
import org.foodie_tour.modules.booking.repository.BookingRepository;
import org.foodie_tour.modules.customer.enums.CustomerStatus;
import org.foodie_tour.modules.customer.repository.CustomerBookingRepository;
import org.foodie_tour.modules.customer.repository.CustomerRepository;
import org.foodie_tour.modules.onepay.service.OnePayService;
import org.foodie_tour.modules.transaction.entity.Transactions;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.foodie_tour.modules.transaction.enums.TransactionStatus;
import org.foodie_tour.modules.transaction.repository.TransactionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnePayServiceImpl implements OnePayService {

    private final BookingRepository bookingRepository;
    private final OnePayConfig onePayConfig;
    private final TransactionsRepository transactionsRepository;
    private final BookingLogRepository bookingLogRepository;
    private final CustomerBookingRepository customerBookingRepository;
    private final CustomerRepository customerRepository;

    @Override
    public String generatePaymentUrl(long bookingId, HttpServletRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        long amountToPay = booking.getIsDeposit() ? (long) (booking.getTotalPrice() * 0.3) : booking.getTotalPrice();

        TreeMap<String, String> vpcParams = new TreeMap<>();
        vpcParams.put("vpc_Version", "2");
        vpcParams.put("vpc_Command", "pay");
        vpcParams.put("vpc_Merchant", onePayConfig.getMerchantId());
        vpcParams.put("vpc_AccessCode", onePayConfig.getAccessCode());
        vpcParams.put("vpc_MerchTxnRef", "Booking-" + bookingId + "-" + System.currentTimeMillis());
        vpcParams.put("vpc_OrderInfo", "Booking-" + bookingId);
        vpcParams.put("vpc_Amount", String.valueOf(amountToPay * 100));
        vpcParams.put("vpc_ReturnURL", onePayConfig.getReturnUrl());
        vpcParams.put("vpc_Locale", "vn");
        vpcParams.put("vpc_IpAddr", getIpAddress(request));

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : vpcParams.entrySet()) {
            if (hashData.length() > 0)
                hashData.append("&");
            hashData.append(entry.getKey()).append("=").append(entry.getValue());
        }

        String secureHash = hmacSHA256(onePayConfig.getHashSecret(), hashData.toString());

        String queryString = vpcParams.entrySet().stream()
                .map(entry -> {
                    try {
                        return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),
                                StandardCharsets.UTF_8.toString());
                    } catch (Exception e) {
                        return entry.getKey() + "=" + entry.getValue();
                    }
                })
                .collect(Collectors.joining("&"));

        return onePayConfig.getPayUrl() + "?" + queryString + "&vpc_SecureHash=" + secureHash.toUpperCase();
    }

    @Override
    @Transactional
    public String processPaymentResponse(Map<String, String> response) {
        String inputHash = response.get("vpc_SecureHash");

        if (inputHash == null || inputHash.isBlank()) {
            throw new RuntimeException("Thiếu chữ ký thanh toán");
        }

        Map<String, String> dataToVerify = new TreeMap<>(response);
        dataToVerify.remove("vpc_SecureHash");
        dataToVerify.remove("vpc_SecureHashType");
        dataToVerify.entrySet()
                .removeIf(entry -> !entry.getKey().startsWith("vpc_") && !entry.getKey().startsWith("user_"));

        String dataString = dataToVerify.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String calculateHash = hmacSHA256(onePayConfig.getHashSecret(), dataString);

        if (!calculateHash.equalsIgnoreCase(inputHash)) {
            throw new RuntimeException("Chữ ký không hợp lệ");
        }

        String orderInfo = response.get("vpc_OrderInfo");
        Long bookingId;
        try {
            bookingId = Long.parseLong(orderInfo.replace("Booking-", "").trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Thông tin đặt lịch không hợp lệ: " + orderInfo);
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không hợp lệ"));

        String txnResponseCode = response.get("vpc_TxnResponseCode");
        boolean isSuccess = "0".equals(txnResponseCode);

        long gatewayTransactionId = 0L;
        String txnNo = response.get("vpc_TransactionNo");
        if (txnNo != null && !txnNo.isBlank()) {
            try {
                gatewayTransactionId = Long.parseLong(txnNo);
            } catch (NumberFormatException ignored) {
            }
        }

        Transactions transactions = Transactions.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .paymentMethod(PaymentMethod.VISA)
                .cashFlow(CashFlow.INCOME)
                .status(isSuccess ? TransactionStatus.SUCCESS : TransactionStatus.FAILED)
                .gatewayTransactionId(gatewayTransactionId)
                .build();
        transactionsRepository.save(transactions);

        if (isSuccess) {
            booking.setBookingStatus(BookingStatus.COMPLETED);
            bookingRepository.save(booking);
            createBookingLog(booking, "Thanh toán Visa thành công");

            customerBookingRepository.findByBooking(booking).ifPresent(customerBooking -> {
                var customer = customerBooking.getCustomer();
                if (customer != null) {
                    customer.setStatus(CustomerStatus.COMPLETED);
                    customerRepository.save(customer);
                }
            });

            return "Thanh toán thành công";
        } else {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            createBookingLog(booking, "Thanh toán thất bại: " + response.getOrDefault("vpc_Message", txnResponseCode));
            return "Thanh toán thất bại";
        }
    }

    private String hmacSHA256(String hexKey, String data) {
        try {
            byte[] keyBytes = Hex.decodeHex(hexKey);
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(keyBytes, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký thanh toán OnePay: " + e.getMessage());
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!StringUtils.hasText(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.hasText(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private void createBookingLog(Booking booking, String description) {
        BookingLog log = BookingLog.builder()
                .booking(booking)
                .description(description)
                .bookingStatus(booking.getBookingStatus())
                .build();
        bookingLogRepository.save(log);
    }
}
