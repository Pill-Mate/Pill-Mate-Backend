package com.example.Pill_Mate_Backend.domain.mypage.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AlarmDTO {
    private Boolean alarmMarketing;
    private Boolean alarmInfo;

    public AlarmDTO(Boolean alarmMarketing, Boolean alarmInfo) {
        this.alarmMarketing = alarmMarketing;
        this.alarmInfo = alarmInfo;
    }
}
