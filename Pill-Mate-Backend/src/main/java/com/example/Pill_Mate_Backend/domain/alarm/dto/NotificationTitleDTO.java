package com.example.Pill_Mate_Backend.domain.alarm.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTitleDTO {
    private Long notificationId;
    private LocalDate notifyDate;
    private LocalTime notifyTime;
    private String title;
    private boolean notificationRead;
    private boolean isFcm;
}
