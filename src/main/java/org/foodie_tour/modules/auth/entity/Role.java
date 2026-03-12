package org.foodie_tour.modules.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.employee.entity.Employee;
import org.foodie_tour.modules.auth.enums.RoleStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"permissions", "employees"})
@NoArgsConstructor
@AllArgsConstructor
@Table()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    Long roleId;

    @Column(name = "name")
    String name;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    RoleStatus status;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonBackReference
    List<Employee> employees = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    List<Permission> permissions = new ArrayList<>();
}
