package org.foodie_tour.modules.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetPasswordRequest {
    @NotBlank(message = "Mật khẩu không được để trống")
    String newPassword;
    @NotBlank(message = "Mật khẩu không được để trống")
    String confirmPassword;
}
