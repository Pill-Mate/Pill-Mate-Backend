package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {
}
