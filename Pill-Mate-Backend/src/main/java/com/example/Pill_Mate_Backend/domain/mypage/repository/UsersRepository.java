package com.example.Pill_Mate_Backend.domain.mypage.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<MedicineSchedule, Long> {
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);
    @Query(value = "select profile_image, username from users" +
            " where email = :email"
            , nativeQuery = true)
    List<Object[]> findUserInfoByEmail(@Param("email") String email);
    @Query(value = "select wakeup_time, bed_time, morning_time, lunch_time, dinner_time from users" +
            " where email = :email"
            , nativeQuery = true)
    List<Object[]> findRoutineByEmail(@Param("email") String email);
    @Query(value = "select alarm_marketing, alarm_info from users" +
            " where email = :email"
            , nativeQuery = true)
    List<Object[]> findAlarmByEmail(@Param("email") String email);
}
