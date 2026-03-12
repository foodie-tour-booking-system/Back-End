package org.foodie_tour.modules.auth.repository;

import org.foodie_tour.modules.auth.entity.Role;
import org.foodie_tour.modules.auth.enums.RoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String name);

    List<Role> findByStatus(RoleStatus status);
}
