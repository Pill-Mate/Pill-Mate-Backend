package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAll();
    Optional<Notification> findById(Long id);

    @Query("""
        SELECT COUNT(n) FROM Notification n
        WHERE NOT EXISTS (
            SELECT 1 FROM NotificationRead nr
            WHERE nr.notification = n AND nr.users.email = :email
        )
    """)
    long countUnreadByUserId(@Param("email") String email);
}
