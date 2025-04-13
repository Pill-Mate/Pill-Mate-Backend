package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUsersIdAndStatus(Long userId, ScheduleStatus status);
    @Query("SELECT s FROM Schedule s JOIN s.medicine m WHERE m.identifyNumber = :identifyNumber")
    Schedule findByIdentifyNumber(@Param("identifyNumber") String identifyNumber);


    Optional<Schedule> findByUsersAndMedicine(Users users, Medicine medicine);
}
