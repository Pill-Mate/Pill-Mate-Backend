package com.example.Pill_Mate_Backend.domain.alarm.dto;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String title;
    private String content;
}
