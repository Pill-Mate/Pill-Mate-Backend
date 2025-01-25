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

    @Column(nullable = false, length = 50)
    private String medicineName;

    @Column(nullable = false, length = 50)
    private String ingredient;

    @Column(nullable = false, length = 255)
    private URI medicineImage;

    @Column(nullable = false, length = 50)
    private String entpName;

    @Column(nullable = false, length = 50)
    private String className;

    @Column(nullable = false, length = 250)
    private String efficacy;

    @Column(nullable = false, length = 250)
    private String sideEffect;

    @Column(nullable = false, length = 250)
    private String caution;

    @Column(nullable = false, length = 250)
    private String storage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private IngredientUnit ingredientUnit;

    @Column(nullable = false, length = 50)
    private Float ingredientAmount;

    //set
    //caution_types
    @Convert(converter = StringSetConverter.class)
    @Column(name = "cautiontypes", columnDefinition = "SET('PREGNANT', 'VOLUME', 'PERIOD', 'OLD', 'AGE', 'ADDITIVE')")
    //, 임부금기, 용량주의, 투여기간주의, 노인주의, 특정연령대금기, 첨가제주의|| <효능군중복, 병용금기> 는 type에 표시안됨.//

    private Set<String> cautionTypes;

    //fk
    //user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    //on delete cascade를 위한 one to many
    @OneToOne(mappedBy = "medicine", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private Schedule schedule;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private List<Hospital> hospitals;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private List<Pharmacy> pharmacies;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE) // Cascade 설정은 부모 쪽에서
    private List<MedicineSchedule> medicineSchedules;
    
}
