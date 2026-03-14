package org.foodie_tour.modules.system.repository;

import org.foodie_tour.modules.system.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
}
