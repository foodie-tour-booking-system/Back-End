package org.foodie_tour.modules.auth.repository;

import org.foodie_tour.modules.auth.entity.Permission;
import org.foodie_tour.modules.auth.enums.PermissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    Optional<Permission> findByName(String name);

    // List<Permission> findByType(PermissionType type);

    List<Permission> findByStatus(PermissionStatus status);
}
