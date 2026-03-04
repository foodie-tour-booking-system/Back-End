package org.foodie_tour.modules.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(
        name = "otp_code",
        indexes = {
                @Index(name = "idx_otp_code", columnList = "otp_code")
        }
)
@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class OtpCode {
    @Id
    @Column(name = "otp_code")
    String otpCode;

    @Column(name = "token", length = 1024)
    String token;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "expired_at")
    @UpdateTimestamp
    LocalDateTime expiredAt;

}
