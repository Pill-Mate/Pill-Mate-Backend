package com.example.Pill_Mate_Backend.domain.oauth2.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apple")
public record AppleProps(
        String baseUrl,
        Auth auth,
        String iss,
        String aud,
        String teamId,
        Key key
) {
    public record Auth(String tokenUrl, String publicKeyUrl) {}
    public record Key(String id, String path) {}
    public String clientId() { return aud; }
    public String keyId() { return key.id(); }
    public String keyPath() { return key.path(); }
}