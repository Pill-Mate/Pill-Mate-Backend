package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


import java.net.URI;
import java.sql.Time;
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

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = true, length = 255)
    private URI profileImage;       //userImage로 바꿔야? -----------------!-------------------

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
    public Users(String username, String email, URI profileImage){
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.wakeupTime = Time.valueOf("08:00:00");
        this.bedTime = Time.valueOf("24:00:00");
        this.morningTime = Time.valueOf("09:00:00");
        this.lunchTime = Time.valueOf("12:00:00");
        this.dinnerTime = Time.valueOf("18:00:00");
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

    //루틴 업데이트
    public Users(String email, Time wakeupTime, Time bedTime, Time morningTime, Time lunchTime, Time dinnerTime) {
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
