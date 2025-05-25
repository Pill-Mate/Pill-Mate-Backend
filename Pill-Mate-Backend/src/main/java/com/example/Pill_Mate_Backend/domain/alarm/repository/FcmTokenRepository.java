package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    //@Query("change... 구현 필요")
    //void deleteByUserId(Long id);

    //@Query("select fcm_token where user_id = :userId")
    @Query("SELECT f.fcmToken FROM FcmToken f WHERE f.users = :userId")// AND f.active = true")
    List<String> findActiveTokensByUserId(@Param("userId") Long userId);

    //@Query("select fcm_token where user_id = :userId")
    @Query("SELECT f.fcmToken FROM FcmToken f WHERE f.users.id = :userId") // AND f.active = true")
    String findActiveTokenByUserId(@Param("userId") Long userId);
}
