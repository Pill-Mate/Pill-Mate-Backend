package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class Pharmacy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String pharmacyName;

    @Column(nullable = false, length = 50)
    private String pharmacyPhone;

    @Column(nullable = false, length = 255)
    private String pharmacyAddress;

    //fk
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;
}
