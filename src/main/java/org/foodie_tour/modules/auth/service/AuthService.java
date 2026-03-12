package org.foodie_tour.modules.auth.service;

import org.foodie_tour.modules.auth.dto.request.LoginRequest;
import org.foodie_tour.modules.auth.dto.request.VerifyPasswordRequest;
import org.foodie_tour.modules.auth.dto.response.IntrospectResponse;
import org.foodie_tour.modules.auth.dto.response.LoginResponse;
import org.foodie_tour.modules.auth.enums.TokenScope;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    IntrospectResponse introspect(String token);
    String getSubjectFromToken(String token);
    String getClaimsFromToken(String token, String claimsName);
    String verifyCurrentPassword(VerifyPasswordRequest request, TokenScope scope, long timeToLive);
    boolean verifyTokenByScope(String token, TokenScope expectedScope);
    void logout();
    String generateTokenByScope(TokenScope tokenScope, long timeToLive, String subject);
}
