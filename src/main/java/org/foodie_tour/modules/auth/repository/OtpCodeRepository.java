package org.foodie_tour.modules.auth.repository;

import org.foodie_tour.modules.auth.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, String> {
}
