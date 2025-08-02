package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.CommonEntity.NotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationReadRepository  extends JpaRepository<NotificationRead, Long> {
    boolean existsByNotificationIdAndUsersId(Long notificationId, Long userId); //읽은 noti있으면 true

    Optional<NotificationRead> findByNotificationIdAndUsersId(Long id, Long id1);

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE (n.userIdNoti = 0 OR n.userIdNoti = :userId) " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM NotificationRead nr " +
            "   WHERE nr.notification.id = n.id AND nr.users.id = :userId" +
            ")")
    Long countUnreadByUserIdOrPublic(@Param("userId") Long userId);
}
