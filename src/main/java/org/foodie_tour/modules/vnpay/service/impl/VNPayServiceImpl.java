package org.foodie_tour.modules.vnpay.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.codec.binary.Hex;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.utils.RandomCode;
import org.foodie_tour.config.VNPayConfig;
import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.entity.BookingLog;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.booking.enums.PaymentMethod;
import org.foodie_tour.modules.booking.mapper.BookingMapper;
import org.foodie_tour.modules.booking.repository.BookingRepository;
import org.foodie_tour.modules.customer.entity.Customer;
import org.foodie_tour.modules.customer.enums.CustomerStatus;
import org.foodie_tour.modules.customer.repository.CustomerBookingRepository;
import org.foodie_tour.modules.customer.repository.CustomerRepository;
import org.foodie_tour.modules.transaction.entity.Transactions;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.foodie_tour.modules.transaction.enums.TransactionStatus;
import org.foodie_tour.modules.transaction.repository.TransactionsRepository;
import org.foodie_tour.modules.vnpay.dto.request.PaymentRequest;
import org.foodie_tour.modules.vnpay.dto.response.VNPayResultResponse;
import org.foodie_tour.modules.vnpay.service.VNPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayServiceImpl implements VNPayService {

    VNPayConfig vnPayConfig;
    BookingRepository bookingRepository;
    TransactionsRepository transactionsRepository;
    CustomerBookingRepository customerBookingRepository;
    CustomerRepository customerRepository;
    BookingMapper bookingMapper;

    @Value("${vnpay.expired-time}")
    @NonFinal
    int EXPIRED_TIME;

    @Value("${vnpay.payment-success-url}")
    @NonFinal
    String SUCCESS_URL;

    @Value("${vnpay.payment-failed-url}")
    @NonFinal
    String FAILED_URL;

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.hasText(ip)) {
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

    public void verifyPaymentRequest(PaymentRequest request, String ipAddress) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        long expectedAmount = booking.getIsDeposit()
                ? (long) (booking.getTotalPrice() * 0.3)
                : booking.getTotalPrice();

        if (expectedAmount != request.getAmount()) {
            throw new InvalidateDataException("Số tiền thanh toán không hợp lệ");
        }

        if (!StringUtils.hasText(ipAddress)) {
            throw new RuntimeException("Không trích xuất được địa chỉ ip");
        }
    }

    public String generatePaymentUrl(PaymentRequest request, HttpServletRequest servletRequest) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        long finalPaymentAmount = booking.getIsDeposit()
                ? (long) (booking.getTotalPrice() * 0.3)
                : booking.getTotalPrice();

        request.setAmount(finalPaymentAmount);
        String ipAddress = getIpAddress(servletRequest);
        verifyPaymentRequest(request, ipAddress);

        try {
            Map<String, String> vnp_params = buildPaymentParams(request, ipAddress);
            String queryUrl = createQueryUrl(vnp_params);
            return vnPayConfig.getPayUrl() + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khởi tạo đường dẫn thanh toán: " + e.getMessage());
        }
    }

    private Map<String, String> buildPaymentParams(PaymentRequest request, String ipAddress) {
        Map<String, String> vnp_Params = new HashMap<>();

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        vnp_Params.put("vnp_Version", VNPayConfig.VERSION);
        vnp_Params.put("vnp_Command", VNPayConfig.COMMAND);
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
        vnp_Params.put("vnp_CurrCode", VNPayConfig.CURR_CODE);

        String txnRef = RandomCode.generateRandomCodeByKey(String.valueOf(request.getBookingId()), 16);
        vnp_Params.put("vnp_TxnRef", txnRef);

        String orderInfo = booking.getBookingCode();
        vnp_Params.put("vnp_OrderInfo", orderInfo);

        String orderType = "Payment Booking";
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", VNPayConfig.LOCALE);
        vnp_Params.put("vnp_ReturnUrl", SUCCESS_URL + "/payment/vnpay/result");
        vnp_Params.put("vnp_IpAddr", ipAddress);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(calendar.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        calendar.add(Calendar.MINUTE, EXPIRED_TIME);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        vnp_Params.put("vnp_BankCode", "NCB");

        return vnp_Params;
    }

    private String createQueryUrl(Map<String, String> vnp_params) {
        List<String> fieldNames = new ArrayList<>(vnp_params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String vnp_SecureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        return query + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    private static String hmacSHA512(String key, String data) {
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            byte[] hash = sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khởi tạo đường dẫn thanh toán");
        }
    }

    public Map<String, String> processIPNFromVnpay(Map<String, String> response) {
        try {
            if (response.isEmpty()) {
                return createIPNResponse("99", "Invalid request");
            }
            checkSum(response);
            return processIPNResult(response);
        } catch (Exception e) {
            return createIPNResponse("99", e.getMessage());
        }
    }

    private void checkSum(Map<String, String> response) {
        if (response.isEmpty()) {
            throw new RuntimeException("Thiếu tham số");
        }

        String vnp_SecureHash = response.get("vnp_SecureHash");
        if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) {
            throw new RuntimeException("Dữ liệu không hợp lệ");
        }

        Map<String, String> fields = new HashMap<>(response);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String signValue = hashAllFields(fields, vnPayConfig.getHashSecret());

        if (!signValue.equals(vnp_SecureHash)) {
            throw new RuntimeException("Dữ liệu không hợp lệ");
        }
    }

    private Map<String, String> createIPNResponse(String rspCode, String message) {
        return Map.of("rspCode", rspCode, "message", message);
    }

    private Map<String, String> processIPNResult(Map<String, String> response) {
        String responseCode = response.get("vnp_ResponseCode");
        String transactionStatus = response.get("vnp_TransactionStatus");

        if (!response.containsKey("vnp_Amount")) {
            return createIPNResponse("99", "Missing amount");
        }
        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            return createIPNResponse("00", "Success");
        }
        return createIPNResponse("99", "Failed");
    }

    private String hashAllFields(Map<String, String> fields, String secretKey) {
        try {
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = fields.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
            return hmacSHA512(secretKey, hashData.toString());
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    @Transactional
    public String processPaymentResponse(Map<String, String> response) {
        VNPayResultResponse result = handlePaymentCallback(response);
        return "SUCCESS".equals(result.getStatus()) ? SUCCESS_URL : FAILED_URL;
    }

    @Transactional
    public VNPayResultResponse handlePaymentCallback(Map<String, String> response) {
        if (response.isEmpty()) {
            throw new RuntimeException("Thiếu tham số");
        }
        try {
            checkSum(response);
            return buildPaymentResult(response);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @Transactional
    public VNPayResultResponse buildPaymentResult(Map<String, String> response) {
        String responseCode = response.get("vnp_ResponseCode");
        String vnpTransactionStatus = response.get("vnp_TransactionStatus");

        if (!response.containsKey("vnp_Amount")) {
            throw new RuntimeException("Thiếu tham số");
        }

        boolean success = "00".equals(responseCode) && "00".equals(vnpTransactionStatus);
        String bookingCode = response.get("vnp_OrderInfo");

        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt lịch không tồn tại"));

        long amount = Long.parseLong(response.get("vnp_Amount")) / 100;

        long expectedAmount = booking.getIsDeposit()
                ? (long) (booking.getTotalPrice() * 0.3)
                : booking.getTotalPrice();

        if (amount != expectedAmount) {
            throw new InvalidateDataException("Thông tin thanh toán không trùng khớp");
        }
        String vnpTransactionNo = response.get("vnp_TransactionNo");

        BookingStatus bookingStatus;
        TransactionStatus transactionStatus;
        String logDescription;
        String status;
        String message;

        Transactions.TransactionsBuilder transactionBuilder = Transactions.builder()
                .booking(booking)
                .amount(amount)
                .paymentMethod(PaymentMethod.VNPAY)
                .cashFlow(CashFlow.INCOME)
                .status(TransactionStatus.PENDING);

        if (vnpTransactionNo != null) {
            transactionBuilder.gatewayTransactionId(Long.parseLong(vnpTransactionNo));
        }

        Transactions transaction = transactionBuilder.build();
        transactionsRepository.save(transaction);

        BookingLog log = BookingLog.builder()
                .booking(booking)
                .build();

        if (success) {
            bookingStatus = BookingStatus.CONFIRMED;
            transactionStatus = TransactionStatus.SUCCESS;
            logDescription = "Thanh toán thành công";
            status = "SUCCESS";
            message = "Payment successful";

            customerBookingRepository.findByBooking(booking).ifPresent(cb -> {
                Customer customer = cb.getCustomer();
                customer.setStatus(CustomerStatus.COMPLETED);
                customerRepository.save(customer);
            });

            booking.setAmountPaid(amount);
            if (!booking.getIsDeposit()) {
                booking.setRemainingAmount(0L);
            }
        } else {
            bookingStatus = BookingStatus.CANCELLED;
            transactionStatus = TransactionStatus.FAILED;
            logDescription = "Thanh toán thất bại";
            status = "FAILED";
            message = "Payment failed";
        }

        transaction.setStatus(transactionStatus);
        booking.setBookingStatus(bookingStatus);

        log.setDescription(logDescription);
        log.setBookingStatus(bookingStatus);
        booking.getBookingLogs().add(log);

        bookingRepository.save(booking);

        return VNPayResultResponse.builder()
                .status(status)
                .message(message)
                .booking(bookingMapper.toResponse(booking))
                .build();
    }
}