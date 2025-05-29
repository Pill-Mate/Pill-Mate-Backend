package com.example.Pill_Mate_Backend.CommonEntity;

import com.example.Pill_Mate_Backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class Notification  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate notifyDate;

    @Column(nullable = false)
    private LocalTime notifyTime;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    //on delete cascade를 위한 one to many
    @ToString.Exclude
    @OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE)
    private List<NotificationRead> notificationReads;
}
