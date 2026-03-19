package org.foodie_tour.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSampleText {
    public static String CREATE_EMPLOYEE_TITLE = "[Felixiter Travel] - Tài khoản nhân viên đã được tạo";
    public static String CREATE_EMPLOYEE_CONTENT = """
    Xin chào,

    Tài khoản của bạn đã được tạo thành công, sử dụng thông tin cung cấp như bên dưới để đăng nhập vào hệ thống:

    Email: %s

    Mật khẩu: %s

    — Đội ngũ Felixiter Travel
    """;

    public static String CREATE_BOOKING_TITLE = "[Felixiter Travel] - Đặt lịch thành công";
    public static String CREATE_BOOKING_CONTENT = """
    Kính gửi Quý khách,
            
    Felixiter Travel xin chân thành cảm ơn Quý khách đã tin tưởng và lựa chọn dịch vụ của chúng tôi.
            
    Chúng tôi xin xác nhận rằng yêu cầu đặt tour của Quý khách đã được thực hiện thành công. Dưới đây là thông tin chi tiết:
            
    Mã đặt tour: %s
            
    Tên khách hàng: %s
            
    Tên tour: %s
            
    Thời gian khởi hành: %s
            
    Số lượng khách: %s
            
    Tổng chi phí: %s
            
    Vui lòng kiểm tra lại thông tin trên. Nếu có bất kỳ sai sót hoặc cần hỗ trợ thêm, Quý khách vui lòng liên hệ với chúng tôi qua:
            
    Email: felixiter.travel@gmail.com
            
    Một lần nữa, xin cảm ơn Quý khách đã lựa chọn Felixiter Travel. Chúng tôi rất mong được đồng hành cùng Quý khách trong chuyến đi sắp tới!
            
    Trân trọng,
    — Đội ngũ Felixiter Travel
    """;

}
