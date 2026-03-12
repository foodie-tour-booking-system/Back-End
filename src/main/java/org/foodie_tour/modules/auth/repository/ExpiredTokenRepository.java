package org.foodie_tour.modules.auth.repository;

import org.foodie_tour.modules.auth.entity.ExpiredToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ExpiredTokenRepository extends JpaRepository<ExpiredToken, String> {
    @Modifying
    @Query("DELETE FROM ExpiredToken e WHERE e.expirationTime < :now")
    void deleteAllExpiredTokens(@Param("now") Date now);
}
