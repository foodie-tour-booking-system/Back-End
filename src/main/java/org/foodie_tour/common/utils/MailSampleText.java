package org.foodie_tour.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSampleText {
    public static String CREATE_EMPLOYEE_TITLE = "Tài khoản nhân viên đã được tạo";
    public static String CREATE_EMPLOYEE_CONTENT = """
    Xin chào,

    Tài khoản của bạn đã được tạo thành công, sử dụng thông tin cung cấp như bên dưới để đăng nhập vào hệ thống:

    Email: %s

    Mật khẩu: %s

    — Đội ngũ Felixiter Travel
    """;

}
