package org.foodie_tour.modules.employee.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeCreateRequest {
    @NotBlank(message = "Tên nhân viên không được để trống")
    @Size(min = 2, max = 100, message = "Tên nhân viên phải từ 2 đến 100 ký tự")
    String employeeName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 50, message = "Email không được vượt quá 50 ký tự")
    String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[3|5|7|8|9])\\d{8}$", message = "Số điện thoại không đúng định dạng")
    String phone;

    @NotNull(message = "Vai trò không được để trống")
    Long roleId;
}
