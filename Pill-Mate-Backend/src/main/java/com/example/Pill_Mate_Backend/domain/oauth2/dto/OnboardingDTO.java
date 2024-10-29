package com.example.Pill_Mate_Backend.domain.oauth2.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;

@Builder
@Data
public class OnboardingDTO {
    //private String email;
    private Time wakeupTime;
    private Time bedTime;
    private Time morningTime;
    private Time lunchTime;

    private Time dinnerTime;
    public Time getWakeupTime() {
        return wakeupTime;
    }

    public Time getBedTime() {
        return bedTime;
    }

    public Time getMorningTime() {
        return morningTime;
    }

    public Time getLunchTime() {
        return lunchTime;
    }

    public Time getDinnerTime() {
        return dinnerTime;
    }

    public void setWakeupTime(Time wakeup_time) {
        this.wakeupTime = wakeupTime;
    }

    public void setBedTime(Time bed_time) {
        this.bedTime = bedTime;
    }

    public void setMorningTime(Time morning_time) {
        this.morningTime = morningTime;
    }

    public void setLunchTime(Time lunch_time) {
        this.lunchTime = lunchTime;
    }

    public void setDinnerTime(Time dinner_time) {
        this.dinnerTime = dinnerTime;
    }

    //public String getEmail() {return email;}

    //public void setEmail(String email) {this.email = email;}
}
