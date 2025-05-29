package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.CommonEntity.NotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationReadRepository  extends JpaRepository<NotificationRead, Long> {
    boolean existsByNotificationIdAndUsersId(Long notificationId, Long userId); //읽은 noti있으면 true

    Optional<NotificationRead> findByNotificationIdAndUsersId(Long id, Long id1);
}
