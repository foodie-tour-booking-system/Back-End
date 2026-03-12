package org.foodie_tour.modules.employee.dto.response;

import lombok.*;
import org.foodie_tour.modules.employee.enums.EmployeeStatus;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeResponse{
    Long employeeId;
    String roleName;
    String employeeName;
    String email;
    String phone;
    String image;
    EmployeeStatus status;
}