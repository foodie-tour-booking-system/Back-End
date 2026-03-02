package org.foodie_tour.common.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class RandomCode {
    static String CHAR_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String generateRandomCode(int length) {
        try {
            String seed = String.valueOf(System.nanoTime());

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(seedBytes);

            StringBuilder code = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int index = (hash[i] & 0xff) % CHAR_POOL.length();
                code.append(CHAR_POOL.charAt(index));
            }
            return code.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available");
        }
    }

    public static String generateRandomCodeByKey(String key, int length) {
        try {
            String seed = key + System.nanoTime();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(seedBytes);

            StringBuilder code = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int index = (hash[i] & 0xff) % CHAR_POOL.length();
                code.append(CHAR_POOL.charAt(index));
            }
            return code.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available");
        }
    }
}