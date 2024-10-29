package com.example.Pill_Mate_Backend.domain.oauth2.service;

import com.example.Pill_Mate_Backend.CommonEntity.User;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.UserInfoResponseDto;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OnboardingService {
    private final UserRepository userRepository;

    public UserInfoResponseDto setUserInfo(String email, Time wakeupTime, Time bedTime, Time morningTime, Time lunchTime, Time dinnerTime) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            //existingUser.setEmail(email);
            existingUser.setWakeupTime(wakeupTime);
            existingUser.setBedTime(bedTime);
            existingUser.setMorningTime(morningTime);
            existingUser.setLunchTime(lunchTime);
            existingUser.setDinnerTime(dinnerTime);
            userRepository.save(existingUser);
        } else {
            // 유저가 존재하지 않을 경우 예외 발생
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        User existingUser = optionalUser.get();
        return new UserInfoResponseDto(existingUser.getUsername(), existingUser.getProfileImage(), email, existingUser.getAlarmMarketing());
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
