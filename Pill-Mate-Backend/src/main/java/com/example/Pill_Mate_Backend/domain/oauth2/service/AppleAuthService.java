package com.example.Pill_Mate_Backend.domain.oauth2.service;

import com.example.Pill_Mate_Backend.domain.oauth2.feign.AppleAuthClient;
import com.example.Pill_Mate_Backend.domain.oauth2.util.ApplePublicKeyGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.lang.reflect.Member;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleAuthService {
    private final AppleAuthClient appleAuthClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final JwtService jwtValidator;

    public String getAppleAccountId(String identityToken)
            throws JsonProcessingException, AuthenticationException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        Map<String, String> headers = jwtValidator.parseHeaders(identityToken);
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers,
                appleAuthClient.getAppleAuthPublicKey());

        return jwtValidator.getTokenClaims(identityToken, publicKey).getSubject();
    }
}