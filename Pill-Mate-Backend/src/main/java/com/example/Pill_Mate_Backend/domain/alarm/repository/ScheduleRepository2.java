package com.example.Pill_Mate_Backend.domain.alarm.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
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
}

