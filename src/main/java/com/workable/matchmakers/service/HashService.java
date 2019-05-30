package com.workable.matchmakers.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    public String bCryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String bCryptedPassord) {
        return passwordEncoder.matches(rawPassword, bCryptedPassord);
    }

    public String sha512(String salt, String rawPassword) {
        return hash("SHA-512", salt, rawPassword);
    }

    public String hash(String algorithm, String salt, String rawPassword) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            if (StringUtils.isNotBlank(salt)) {
                md.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] bytes = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
