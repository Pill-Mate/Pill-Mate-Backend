package com.example.Pill_Mate_Backend.domain.alarm.dto;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFcmTokenDTO {
    private String fcmToken;
}
