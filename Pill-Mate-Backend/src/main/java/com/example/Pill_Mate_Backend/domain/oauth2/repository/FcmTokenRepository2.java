package com.example.Pill_Mate_Backend.domain.oauth2.repository;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FcmTokenRepository2 extends JpaRepository<FcmToken, Long> {
    @Query("SELECT f.fcmToken FROM FcmToken f WHERE f.users.email = :email")//"select fcm_token from fcm_token join users where email = :email")// AND f.active = true")
    List<String> findFcmTokenByEmail(@Param("email") String email);

}