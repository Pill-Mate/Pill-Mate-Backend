package com.example.Pill_Mate_Backend.domain.mypage.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineScheduleRepository3 extends JpaRepository<MedicineSchedule, Long> {
    @Query(value = "SELECT * FROM medicine_schedule s " +
            "WHERE user_id = :userId " +
            "AND (s.intake_date > CURRENT_DATE " +
            "     OR (s.intake_date = CURRENT_DATE AND s.intake_time > CURRENT_TIME))",
            nativeQuery = true)
    List<MedicineSchedule> findByUsers(@Param("userId") Long userId);
}
