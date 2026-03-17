package org.foodie_tour.modules.employee.repository;

import org.foodie_tour.modules.employee.entity.Employee;
import org.foodie_tour.modules.employee.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhone(String phone);

    List<Employee> findByStatus(EmployeeStatus status);
}
