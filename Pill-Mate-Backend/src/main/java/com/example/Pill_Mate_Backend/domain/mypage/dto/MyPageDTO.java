package com.example.Pill_Mate_Backend.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {
    private String userName;
    private String email;
    private String profileImage;
    private Boolean alarmMarketing;
    private Boolean alarmInfo;
}
