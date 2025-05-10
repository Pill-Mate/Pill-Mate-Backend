package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository2 extends JpaRepository<Schedule, Long> {
    //@Query("Select s.start_date, s.intake_period, u.id, m.medicine_name from schedule s join users u join medicine m" +
    //        " where s.is_alarm=true and u.email=:email")
    @Query("SELECT s.startDate, s.intakePeriod, u.id, m.medicineName " +
            "FROM Schedule s " +
            "JOIN s.users u " +
            "JOIN s.medicine m " +
            "WHERE s.isAlarm = true AND u.email = :email")
    List<Object[]> findByIsAlarmTrue(@Param("email") String email);

    @Query("""
            SELECT new com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO(
                ms.userId, ms.intakeDate, ms.intakeTime
            )
            FROM Schedule s
            JOIN MedicineSchedule ms
            WHERE ms.eatCheck = false
              AND s.isAlarm = true
              AND ms.intakeDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 1
            """)
    List<AlarmScheduleDTO> findNextDayAlarms();

    @Query("""
            SELECT new com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO(
                ms.userId, ms.intakeDate, ms.intakeTime
            )
            FROM Schedule s
            JOIN MedicineSchedule ms
            WHERE ms.eatCheck = false
              AND s.isAlarm = true
              AND ms.intakeDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 1
              AND ms.userId = :userId
            """)
    List<AlarmScheduleDTO> findNextDayAlarmsById(@Param("userId") Long userId);
}

