package org.foodie_tour.modules.auth.dto.response;

import lombok.*;
import org.foodie_tour.modules.auth.entity.Permission;
import org.foodie_tour.modules.auth.enums.RoleStatus;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RoleResponse {
    private Long roleId;
    private String name;
    private RoleStatus status;
    private List<PermissionDto> permissionDtoList;

    public record PermissionDto(long permissionId, String name) {
        public static PermissionDto from(Permission permission) {
            return new PermissionDto(
                    permission.getPermissionId(),
                    permission.getName()
                    //permission.getType()
            );
        }
    }
}