package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import com.example.Pill_Mate_Backend.CommonEntity.enums.EatUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeCount;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
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
    private IntakeCount intakeCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = true)
    private MealUnit mealUnit;

    @Column(nullable = false, length = 50)
    private Integer mealTime;

    @Column(nullable = false)
    private Boolean eatCheck;//변수명 check 시 에러 -> eatCheck로 변경.


    //fk
    //user_id, medicine_id
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;
}
