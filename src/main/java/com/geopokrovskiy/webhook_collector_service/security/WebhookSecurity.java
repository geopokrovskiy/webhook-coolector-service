package com.geopokrovskiy.webhook_collector_service.security;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Component
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "webhook-security")
public class WebhookSecurity {

    private HashMap<String, String> providerSecretMap;

    private final HMACEncoder hmacEncoder;

    public boolean validateXSignature(String xSignatureHeader, String rawData, String providerName) throws NoSuchAlgorithmException,
            InvalidKeyException {
        if (xSignatureHeader == null || providerName == null) return false;
        String secret = providerSecretMap.get(providerName);
        if (secret == null) {
            throw new InvalidKeyException();
        }
        return xSignatureHeader.equals(hmacEncoder.calculateHMACSignature(rawData, secret));
    }
}
