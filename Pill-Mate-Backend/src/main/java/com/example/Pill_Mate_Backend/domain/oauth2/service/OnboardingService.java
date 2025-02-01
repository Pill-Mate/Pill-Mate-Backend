package com.example.Pill_Mate_Backend.domain.oauth2.service;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
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

    public UserInfoResponseDto setUserInfo(String email, Time wakeupTime, Time bedTime, Time morningTime, Time lunchTime, Time dinnerTime, Boolean alarmMarketing, Boolean alarmInfo) {

        Optional<Users> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            Users existingUsers = optionalUser.get();
            existingUsers.setWakeupTime(wakeupTime);
            existingUsers.setBedTime(bedTime);
            existingUsers.setMorningTime(morningTime);
            existingUsers.setLunchTime(lunchTime);
            existingUsers.setDinnerTime(dinnerTime);
            existingUsers.setAlarmMarketing(alarmMarketing);
            existingUsers.setAlarmInfo(alarmInfo);
            userRepository.save(existingUsers);
        } else {
            // 유저가 존재하지 않을 경우 예외 발생
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        Users existingUsers = optionalUser.get();
        return new UserInfoResponseDto(existingUsers.getUsername(), existingUsers.getProfileImage(), email);
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
