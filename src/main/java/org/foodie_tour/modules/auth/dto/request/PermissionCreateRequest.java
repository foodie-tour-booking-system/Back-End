package org.foodie_tour.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequest {
    @NotBlank(message = "Tên quyền không được để trống")
    @Size(min = 2, max = 100, message = "Tên quyền phải từ 2 đến 100 ký tự")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

/*    @NotNull(message = "Loại quyền không được để trống")
    private PermissionType type;*/
}