package com.example.Pill_Mate_Backend.domain;

import com.example.Pill_Mate_Backend.domain.common.BaseEntity;
import com.example.Pill_Mate_Backend.domain.enums.EatUnit;
import com.example.Pill_Mate_Backend.domain.enums.MealUnit;
import com.example.Pill_Mate_Backend.domain.enums.MedicineUnit;
import com.example.Pill_Mate_Backend.domain.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.sets.StringSetConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private MealUnit mealUnit;

    @Column(nullable = false, length = 50)
    private Integer mealTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private EatUnit eatUnit;

    @Column(nullable = false, length = 50)
    private Integer eatCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private MedicineUnit medicineUnit;

    @Column(nullable = false, length = 50)
    private Float medicineVolume;

    @Column(nullable = false)
    private Boolean isAlarm;

    @Column(nullable = false)
    @ColumnDefault("ACTIVATE")
    private ScheduleStatus status;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false, length = 50)
    private Integer intakePeriod;

    //set
    //intake_frequency, intake_count
    @Convert(converter = StringSetConverter.class)
    @Column(name = "intakefrequency", columnDefinition = "SET('MON', 'TUE', 'WEN', 'THU', 'FRI', 'SAT', 'SUN')")
    //월화수목금토일
    private Set<String> intakeFrequency;

    @Convert(converter = StringSetConverter.class)
    @Column(name = "intakecount", columnDefinition = "SET('MORNING', 'LUNCH', 'DINNER', 'EMPTY', 'SLEEP')")
    //아점저공취
    private Set<String> intakeCount;

    //fk
    //user_id, medicine_id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;
}
