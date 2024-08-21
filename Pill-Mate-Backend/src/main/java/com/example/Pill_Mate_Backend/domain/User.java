package com.example.Pill_Mate_Backend.domain;

import com.example.Pill_Mate_Backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;
import java.util.Date;

public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String sex;
    //이건 enum으로 안하나

    @Column(nullable = false)
    private Date birth;

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

}
