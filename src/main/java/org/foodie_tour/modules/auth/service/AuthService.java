package org.foodie_tour.modules.auth.service;

public interface AuthService {
    String generateRelocateRequestToken(String bookingCode);
    String getSubjectFromToken(String token);
}
