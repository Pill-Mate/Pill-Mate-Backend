package com.example.Pill_Mate_Backend.domain.oauth2.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final String secretKey;
    private final long expirationTime;

    // 생성자를 통한 의존성 주입
    public JwtService(@Value("${jwt.secret-key}") String secretKey,
                      @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());  // Base64 인코딩된 비밀 키
        this.expirationTime = expirationTime;
    }

    // JWT 토큰 생성
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // JWT에서 이메일 추출
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}