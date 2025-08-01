package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


import java.net.URI;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true, length = 100)
    private String appleId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = true, length = 255)
    private URI profileImage;       //userImage로 바꿔야? -----------------!-------------------

    @Column(nullable = true)
    private LocalTime wakeupTime;

    @Column(nullable = true)
    private LocalTime bedTime;

    @Column(nullable = true)
    private LocalTime morningTime;

    @Column(nullable = true)
    private LocalTime lunchTime;

    @Column(nullable = true)
    private LocalTime dinnerTime;

    @Column(nullable = true)
    private Boolean alarmMarketing;

    @Column(nullable = true)
    private Boolean alarmInfo;


    //카카오 로그인 엔티티 생성할때 디폴트 값 넣어서 생성.
    public Users(String username, String email, URI profileImage){
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.appleId = null;
        this.wakeupTime = null;
        this.bedTime = null;
        this.morningTime = null;
        this.lunchTime = null;
        this.dinnerTime = null;
        this.alarmMarketing = false;
        this.alarmInfo = false;
    }

    public Users(String username, String email, String appleId){
        this.username = username;
        this.email = email;
        this.appleId = appleId;
        this.profileImage = null;
        this.wakeupTime = null;
        this.bedTime = null;
        this.morningTime = null;
        this.lunchTime = null;
        this.dinnerTime = null;
        this.alarmMarketing = false;
        this.alarmInfo = false;
    }

    //on delete cascade를 위한 one to many
    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Medicine> medicines;

    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Schedule> schedules;


    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Pharmacy> pharmacies;

    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private List<Hospital> hospitals;

    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private List<MedicineSchedule> medicineSchedules;
    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private List<FcmToken> fcmTokens;

    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<RefreshToken> refreshTokens;

    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<NotificationRead> notificationReads;

    //루틴 업데이트
    public Users(String email, LocalTime wakeupTime, LocalTime bedTime, LocalTime morningTime, LocalTime lunchTime, LocalTime dinnerTime) {
        this.email = email;
        this.wakeupTime = wakeupTime;
        this.bedTime = bedTime;
        this.morningTime = morningTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
    }
    //알람 업데이트
    public Users(String email, Boolean alarmMarketing, Boolean alarmInfo) {
        this.email = email;
        this.alarmMarketing = alarmMarketing;
        this.alarmInfo = alarmInfo;
    }
}
