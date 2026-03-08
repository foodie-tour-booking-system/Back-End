package org.foodie_tour.modules.auth.repository;

import org.foodie_tour.modules.auth.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, String> {
    @Query(value = "SELECT otp.otpCode FROM OtpCode otp WHERE otp.expiredAt < :now")
    List<String> findAllExpiredOtp(@Param("now") LocalDateTime now);
}
