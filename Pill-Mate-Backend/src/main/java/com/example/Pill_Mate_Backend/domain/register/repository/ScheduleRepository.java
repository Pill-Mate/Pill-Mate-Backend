package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
