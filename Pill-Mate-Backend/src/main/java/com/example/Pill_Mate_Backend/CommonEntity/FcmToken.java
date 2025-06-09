package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class FcmToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //fk
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(nullable = false, length = 1024)
    private String fcmToken;

    //@Column(length = 20)
    //private String deviceType;  ----이걸 해야.. 여러기기 등록 가능.

    //@Column(length = 100)
    //private String deviceModel;

    //@Column
    //private boolean isActive;
}