package org.foodie_tour.modules.employee.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.auth.entity.Role;
import org.foodie_tour.modules.booking.entity.BookingLog;
import org.foodie_tour.modules.employee.enums.EmployeeStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"role", "bookingLogs"})
@NoArgsConstructor
@AllArgsConstructor
@Table()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonManagedReference
    List<BookingLog> bookingLogs = new ArrayList<>();

    @Column(name = "employee_name")
    String employeeName;

    @Column(name = "email", length = 50, unique = true)
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "image")
    String image;

    @Column(name = "password")
    String password;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    EmployeeStatus status;
}
