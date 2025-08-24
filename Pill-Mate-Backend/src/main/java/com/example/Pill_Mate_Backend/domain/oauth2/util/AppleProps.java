package com.example.Pill_Mate_Backend.domain.oauth2.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apple")
public record AppleProps(String baseUrl, String teamId, String keyId, String clientId, String privateKeyPem) {}