package com.example.Pill_Mate_Backend.domain.oauth2.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query(value = "SELECT token from refresh_token rf join users u on uid = rf.user_id"
            +"WHERE u.email = :email",
            nativeQuery = true)
    String findRefreshTokenByEmail(@Param("email") String email);
    @Query(value = "SELECT rf.* from refresh_token rf join users u on u.id = rf.user_id"
            +" WHERE u.email = :email",
            nativeQuery = true)
    RefreshToken findByEmail(@Param("email") String email);
}
