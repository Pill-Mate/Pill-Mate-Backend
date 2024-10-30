package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.CommonEntity.enums.*;
import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import com.example.Pill_Mate_Backend.CommonEntity.sets.StringSetConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
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
    private IngredientUnit medicineUnit;

    @Column(nullable = false, length = 50)
    private Float medicineVolume;

    @Column(nullable = false)
    private Boolean isAlarm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ACTIVATE'")
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
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;
}
