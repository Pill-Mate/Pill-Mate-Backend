package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IngredientUnit;
import com.example.Pill_Mate_Backend.CommonEntity.sets.StringSetConverter;
import jakarta.persistence.*;
import lombok.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class Medicine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String identifyNumber;

    @Column(nullable = false, length = 255)
    private String medicineName;

    @Column(nullable = false, length = 50)
    private String ingredient;

    @Column(nullable = false, length = 255)
    private URI medicineImage;

    @Column(nullable = false, length = 50)
    private String entpName;

    @Column(nullable = false, length = 50)
    private String className;

    //nullable 임시 수정
    @Column(nullable = true, length = 250)
    private String efficacy;

    //nullable 임시 ㅣ수정
    @Column(nullable = true, length = 250)
    private String sideEffect;

    //nullable 임시 수정
    @Column(nullable = true, length = 250)
    private String caution;

    //nullable 임시 수정
    @Column(nullable = true, length = 250)
    private String storage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, columnDefinition = "VARCHAR(10)")
    private IngredientUnit ingredientUnit;

    @Column(nullable = true, length = 50)
    private Float ingredientAmount;

    //set
    //caution_types
    @Convert(converter = StringSetConverter.class)
    @Column(name = "cautiontypes", columnDefinition = "SET('PREGNANT', 'VOLUME', 'PERIOD', 'OLD', 'AGE', 'ADDITIVE')")
    // 임부금기, 용량주의, 투여기간주의, 노인주의, 특정연령대금기, 첨가제주의|| <효능군중복, 병용금기> 는 type에 표시안됨.//

    private Set<String> cautionTypes;

    //fk
    //user_id
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ToString.Exclude
    @OneToOne(mappedBy = "medicine", cascade = CascadeType.REMOVE)
    private Schedule schedule;


    @ToString.Exclude
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE)
    private List<Hospital> hospitals;

    @ToString.Exclude
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE)
    private List<Pharmacy> pharmacies;

    @ToString.Exclude
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE)
    private List<MedicineSchedule> medicineSchedules;

}
