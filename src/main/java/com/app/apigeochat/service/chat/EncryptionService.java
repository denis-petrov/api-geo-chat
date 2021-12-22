package com.app.apigeochat.service.chat;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Transactional
@Service
public class EncryptionService {
    public String ENCRYPTION_ALGORITHM = "SHA-512";

    @SneakyThrows
    public String encrypt(String str) {
        MessageDigest md = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
        byte[] encryptedData = md.digest(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }
}
