package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Time;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private Time wakeupTime;

    @Column(nullable = false)
    private Time bedTime;

    @Column(nullable = false)
    private Time morningTime;

    @Column(nullable = false)
    private Time lunchTime;

    @Column(nullable = false)
    private Time dinnerTime;

    @Column(nullable = false)
    private Boolean alarmMarketing;

    @Column(nullable = false)
    private Boolean alarmInfo;


    //카카오 로그인 엔티티 생성할때 디폴트 값 넣어서 생성.
    public User(String username, String email, Boolean alarmMarketing, String profileImage){
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.wakeupTime = Time.valueOf("08:00:00");
        this.bedTime = Time.valueOf("24:00:00");
        this.morningTime = Time.valueOf("09:00:00");
        this.lunchTime = Time.valueOf("12:00:00");
        this.dinnerTime = Time.valueOf("18:00:00");
        this.alarmMarketing = alarmMarketing;
        this.alarmInfo = false;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

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

    public Boolean getAlarmMarketing() {
        return alarmMarketing;
    }

    public Boolean getAlarmInfo() {
        return alarmInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWakeupTime(Time wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public void setBedTime(Time bedTime) {
        this.bedTime = bedTime;
    }

    public void setMorningTime(Time morningTime) {
        this.morningTime = morningTime;
    }

    public void setLunchTime(Time lunchTime) {
        this.lunchTime = lunchTime;
    }

    public void setDinnerTime(Time dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public void setAlarmMarketing(Boolean alarmMarketing) {
        this.alarmMarketing = alarmMarketing;
    }

    public void setAlarmInfo(Boolean alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
