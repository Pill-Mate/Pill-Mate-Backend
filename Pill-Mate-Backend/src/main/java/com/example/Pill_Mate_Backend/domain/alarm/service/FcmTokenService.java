package com.example.Pill_Mate_Backend.domain.alarm.service;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.alarm.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FcmTokenService {/*
    private final FcmTokenRepository fcmTokenRepository;

    public void registerToken(Users user, String token, String deviceType) {
        // 기존 토큰이 있다면 비활성화
        //fcmTokenRepository.deactivateUserTokens(user.getId());

        // 새 토큰 등록
        FcmToken fcmToken = FcmToken.builder()
                .user(user)
                .fcmToken(token)
                //.deviceType(deviceType)
                //.isActive(true)
                .build();

        fcmTokenRepository.save(fcmToken)
    }

    public List<String> getActiveTokens(Long userId) {
        return fcmTokenRepository.findActiveTokensByUserId(userId)
                .stream()
                .map(FcmToken::getFcmToken)
                .collect(Collectors.toList());
    }*/
}