package com.example.Pill_Mate_Backend.domain;

import com.example.Pill_Mate_Backend.domain.common.BaseEntity;
import com.example.Pill_Mate_Backend.domain.enums.IngredientUnit;
import com.example.Pill_Mate_Backend.domain.sets.StringSetConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    private String name;

    @Column(nullable = false, length = 50)
    private String ingredient;

    @Column(nullable = false, length = 50)
    private String image;

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
    private User user;
}
