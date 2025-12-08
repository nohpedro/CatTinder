package com.example.configserver.web;

import java.util.Collections;
import java.util.Map;

import org.springframework.cloud.config.server.encryption.TextEncryptorLocator;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final TextEncryptorLocator textEncryptorLocator;

    public CryptoController(TextEncryptorLocator textEncryptorLocator) {
        this.textEncryptorLocator = textEncryptorLocator;
    }

    @PostMapping("/encrypt")
    public Map<String, String> encrypt(@RequestBody String value) {
        String sanitized = value == null ? "" : value.trim();
        String cipher = encryptor().encrypt(sanitized);
        return Map.of("cipher", cipher);
    }

    @PostMapping("/decrypt")
    public Map<String, String> decrypt(@RequestBody String cipher) {
        String sanitized = cipher == null ? "" : cipher.trim();
        String plain = encryptor().decrypt(sanitized);
        return Map.of("value", plain);
    }

    private TextEncryptor encryptor() {
        return textEncryptorLocator.locate(Collections.emptyMap());
    }
}

