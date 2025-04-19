package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {
    @Query("SELECT DISTINCT ms.intakeTime FROM MedicineSchedule ms " +
            "WHERE ms.users.id = :userId AND ms.medicine.id = :medicineId " +
            "ORDER BY ms.intakeTime ASC")
    List<LocalTime> findDistinctIntakeTimes(@Param("userId") Long userId,
                                            @Param("medicineId") Long medicineId);
    // MedicineScheduleRepository.java
    void deleteBySchedule(Schedule schedule);


}
