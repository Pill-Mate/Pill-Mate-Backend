package com.example.Pill_Mate_Backend.domain;

import com.example.Pill_Mate_Backend.domain.common.BaseEntity;
import com.example.Pill_Mate_Backend.domain.enums.EatUnit;
import com.example.Pill_Mate_Backend.domain.enums.IntakeSpecific;
import com.example.Pill_Mate_Backend.domain.enums.MealUnit;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

public class MedicineSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date intakeDate;

    @Column(nullable = false)
    private Time intakeTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private EatUnit eatUnit;

    @Column(nullable = false, length = 50)
    private Integer eatCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private IntakeSpecific intakeSpecific;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private MealUnit mealUnit;

    @Column(nullable = false, length = 50)
    private Integer mealTime;

    @Column(nullable = false)
    private Boolean check;

    //fk
    //user_id, medicine_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;
}
