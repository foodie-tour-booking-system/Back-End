package org.foodie_tour.modules.auth.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.modules.auth.dto.request.LoginRequest;
import org.foodie_tour.modules.auth.dto.request.VerifyPasswordRequest;
import org.foodie_tour.modules.auth.dto.response.IntrospectResponse;
import org.foodie_tour.modules.auth.dto.response.LoginResponse;
import org.foodie_tour.modules.auth.entity.ExpiredToken;
import org.foodie_tour.modules.auth.enums.TokenScope;
import org.foodie_tour.modules.auth.repository.ExpiredTokenRepository;
import org.foodie_tour.modules.auth.repository.OtpCodeRepository;
import org.foodie_tour.modules.auth.service.AuthService;
import org.foodie_tour.modules.employee.entity.Employee;
import org.foodie_tour.modules.auth.entity.Role;
import org.foodie_tour.modules.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    EmployeeRepository employeeRepository;
    OtpCodeRepository otpCodeRepository;
    ExpiredTokenRepository expiredTokenRepository;
    PasswordEncoder passwordEncoder;

    String PHONE_REGEX = "^\\d+$";
    String EMAIL_REGEX = "^.*@.*$";

    @Value("${auth.key}")
    @NonFinal
    String KEY;

    @Value("${auth.issuer}")
    @NonFinal
    String ISSUER;

    public Employee getCurrentEmployee() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long employeeId = Long.parseLong(jwt.getSubject());
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tài khoản không tồn tại"));
    }

    public LoginResponse login(LoginRequest request) {
        String input = request.getInput();

        Employee employee;

        if (input.matches(EMAIL_REGEX)) {
            employee = employeeRepository.findByEmail(input).orElse(null);
        } else if (input.matches(PHONE_REGEX)) {
            employee = employeeRepository.findByPhone(input).orElse(null);
        } else {
            throw new InvalidateDataException("Vui lòng nhập email hoặc số điện thoại hợp lệ!");
        }

        if (employee == null) {
            throw new ResourceNotFoundException("Tài khoản không tồn tại!");
        }

        switch (employee.getStatus()) {
            case ACTIVE -> {}
            case INACTIVE -> throw new InvalidateDataException("Tài khoản không được kích hoạt, liên hệ người quản trị để được hỗ trợ!");
            case DELETED -> throw new ResourceNotFoundException("Tài khoản không tồn tại!");
        }

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new InvalidateDataException("Mật khẩu không chính xác!");
        }

        return LoginResponse.builder()
                .token(generateToken(employee))
                .build();
    }

    public String generateToken(Employee employee) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(employee.getEmployeeId()))
                .jwtID(UUID.randomUUID().toString())
                .claim("email", employee.getEmail())
                .claim("phone", employee.getPhone())
                .claim("scope", buildScope(employee.getRole()))
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    private String buildScope(Role role) {
        StringJoiner scope = new StringJoiner(" ");

        scope.add("ROLE_" + role.getName());

        role.getPermissions().forEach(permission -> {
            scope.add(permission.getName());
        });

        return scope.toString();
    }

    public String getSubjectFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Lỗi khi đọc dữ liệu từ token");
        }
    }

    public String getClaimsFromToken(String token, String claimsName) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getClaim(claimsName).toString();
        } catch (ParseException e) {
            throw new RuntimeException("Lỗi khi đọc dữ liệu từ token");
        }
    }

    public String verifyCurrentPassword(VerifyPasswordRequest request, TokenScope scope, long timeToLive) {
        Employee employee = getCurrentEmployee();

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new InvalidateDataException("Mật khẩu không chính xác!");
        }

        return generateTokenByScope(scope, timeToLive, String.valueOf(employee.getEmployeeId()));
    }

    @Transactional
    public boolean verifyTokenByScope(String token, TokenScope expectedScope) {
        try {
            JWSVerifier verifier = new MACVerifier(KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean verified = signedJWT.verify(verifier);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
            String tokenScopeClaim = signedJWT.getJWTClaimsSet().getStringClaim("tokenScope");

            if (!verified || !new Date().before(expiration)) {
                return false;
            }

            if (expiredTokenRepository.existsById(tokenId)) {
                return false;
            }

            if (!expectedScope.name().equals(tokenScopeClaim)) {
                return false;
            }

            expiredTokenRepository.save(new ExpiredToken(tokenId, expiration));
            return true;
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    public String generateTokenByScope(TokenScope tokenScope, long timeToLive, String subject) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .jwtID(UUID.randomUUID().toString())
                .claim("tokenScope", tokenScope)
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(timeToLive, ChronoUnit.MINUTES).toEpochMilli()))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Lỗi khởi tạo token");
        }
    }

    public IntrospectResponse introspect(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean verified = signedJWT.verify(verifier);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            String tokenId = signedJWT.getJWTClaimsSet().getJWTID();

            boolean isLoggedOut = expiredTokenRepository.existsById(tokenId);

            return IntrospectResponse.builder()
                    .valid(verified && new Date().before(expiration) && !isLoggedOut)
                    .build();
        } catch (JOSEException | ParseException e) {
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }
    }

    @Transactional
    public void logout() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String tokenId = jwt.getId();
        Date expirationTime = Date.from(jwt.getExpiresAt());

        ExpiredToken expiredToken = new ExpiredToken(tokenId, expirationTime);
        expiredTokenRepository.save(expiredToken);
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void autoCleanUpOtp() {
        List<String> otpList = otpCodeRepository.findAllExpiredOtp(LocalDateTime.now());
        otpCodeRepository.deleteAllById(otpList);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoCleanUpExpiredToken() {
        expiredTokenRepository.deleteAllExpiredTokens(new Date());
    }



}
