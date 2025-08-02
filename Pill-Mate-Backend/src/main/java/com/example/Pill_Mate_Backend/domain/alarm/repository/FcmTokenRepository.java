package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM FcmToken f WHERE f.users.id = :id")
    void deleteByUserId(@Param("id") Long id);

    //@Query("select fcm_token where user_id = :userId")
    @Query("SELECT f.fcmToken FROM FcmToken f WHERE f.users.id = :userId")// AND f.active = true")
    List<String> findActiveTokensByUserId(@Param("userId") Long userId);

    //@Query("select fcm_token where user_id = :userId")
    @Query("SELECT f.fcmToken FROM FcmToken f WHERE f.users.id = :userId") // AND f.active = true")
    String findActiveTokenByUserId(@Param("userId") Long userId);

    @Query("SELECT f.fcmToken FROM FcmToken f WHERE f.users.email = :email")//"select fcm_token from fcm_token join users where email = :email")// AND f.active = true")
    List<String> findFcmTokenByEmail(@Param("email") String email);

    boolean existsByUsersAndFcmToken(Users users, String fcmToken);

    @Modifying(clearAutomatically = true)
    @Transactional  // 여기 트랜잭션이 반드시 붙어야 함!
    @Query("DELETE FROM FcmToken f WHERE f.fcmToken = :fcmToken")
    void deleteByFcmToken(String fcmToken);

    boolean existsByUsersIdAndFcmToken(Long userId, String fcmToken);
}
