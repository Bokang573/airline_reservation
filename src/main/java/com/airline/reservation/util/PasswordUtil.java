package com.airline.reservation.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashPassword(String password) throws Exception {
        byte[] salt = new byte[8];
        RANDOM.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashed = md.digest(password.getBytes("UTF-8"));
        byte[] combined = new byte[salt.length + hashed.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hashed, 0, combined, salt.length, hashed.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static boolean verifyPassword(String password, String stored) throws Exception {
        byte[] combined = Base64.getDecoder().decode(stored);
        byte[] salt = new byte[8];
        System.arraycopy(combined, 0, salt, 0, salt.length);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashed = md.digest(password.getBytes("UTF-8"));
        byte[] expected = new byte[hashed.length];
        System.arraycopy(combined, salt.length, expected, 0, hashed.length);
        if (hashed.length != expected.length) return false;
        int diff = 0;
        for (int i = 0; i < hashed.length; i++) diff |= hashed[i] ^ expected[i];
        return diff == 0;
    }
}

