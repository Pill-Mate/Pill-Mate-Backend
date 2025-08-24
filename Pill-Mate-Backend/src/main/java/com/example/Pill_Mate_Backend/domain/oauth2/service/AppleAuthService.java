package com.example.Pill_Mate_Backend.domain.oauth2.service;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.ApplePublicKeyResponse;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.AppleTokenRes;
import com.example.Pill_Mate_Backend.domain.oauth2.feign.AppleAuthClient;
import com.example.Pill_Mate_Backend.domain.oauth2.feign.AppleKeysClient;
import com.example.Pill_Mate_Backend.domain.oauth2.feign.AppleOAuthClient;
import com.example.Pill_Mate_Backend.domain.oauth2.util.AppleClientSecretProvider;
import com.example.Pill_Mate_Backend.domain.oauth2.util.AppleProps;
import com.example.Pill_Mate_Backend.domain.oauth2.util.ApplePublicKeyGenerator;
import com.example.Pill_Mate_Backend.domain.oauth2.util.TokenCipher;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.lang.reflect.Member;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppleAuthService {
    private final AppleAuthClient appleAuthClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final JwtService jwtValidator;
    private final AppleKeysClient keysClient;
    private final AppleOAuthClient oauthClient;
    private final AppleProps appleProps;
    private final TokenCipher tokenCipher;
    private final UserRepository userRepository;

    public String getAppleAccountId(String identityToken)
            throws JsonProcessingException, AuthenticationException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        Map<String, String> headers = jwtValidator.parseHeaders(identityToken);
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers,
                appleAuthClient.getAppleAuthPublicKey());

        return jwtValidator.getTokenClaims(identityToken, publicKey).getSubject();
    }

    public ApplePublicKeyResponse fetchKeys() {
        return keysClient.getApplePublicKeys();
    }

    public String exchange(String authorizationCode) {
        String clientSecret = new AppleClientSecretProvider(appleProps).issueClientSecret(180);
        Map<String, Object> form = new HashMap<>();
        form.put("grant_type", "authorization_code");
        form.put("code", authorizationCode);
        form.put("client_id", appleProps.clientId());
        form.put("client_secret", clientSecret);
        AppleTokenRes res = oauthClient.exchangeToken(form); //apple한테 refreshtoken 받아오기
        String enc = tokenCipher.encrypt(res.refresh_token());//refresh token 암호화
        return enc;
    }

    public void revoke(String email) {
        Optional<Users> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            Users users = existingUser.get();
            String enc = users.getAppleRefreshToken();
            //AppleAccount acc = appleAccountRepo.findByUserId(userId).orElseThrow();
            if(enc == null || "".equals(enc)){
                System.out.println("애플 리프레쉬 토큰이 없음. 애플 연동 해제 불가능");
                throw new GeneralException(ErrorStatus._APPLE_REFRESH_TOKEN_NULL);
            }
            String refreshToken = tokenCipher.decrypt(enc);
            String clientSecret = new AppleClientSecretProvider(appleProps).issueClientSecret(7);
            Map<String, Object> form = new HashMap<>();
            form.put("client_id", appleProps.clientId());
            form.put("client_secret", clientSecret);
            form.put("token", refreshToken);
            form.put("token_type_hint", "refresh_token");
            oauthClient.revoke(form);
            //appleAccountRepo.delete(acc);
        }
    }
}