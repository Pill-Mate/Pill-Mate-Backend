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
    @Query(value = """
            SELECT DISTINCT s.start_date, s.intake_period, s.user_id, m.medicine_name
            FROM schedule s
            JOIN medicine m ON s.medicine_id = m.id
            WHERE s.is_alarm = true
            """, nativeQuery = true)
    List<Object[]> findByIsAlarmTrue();

    @Query(value = """
            select distinct mc.user_id, mc.intake_date, mc.intake_time 
            from schedule sc 
            join medicine_schedule mc ON mc.schedule_id = sc.id 
            join users u ON u.id = mc.user_id 
            where mc.eat_check = false 
                and sc.is_alarm = true 
                and mc.intake_date BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY)
                and u.alarm_info = true
            """, nativeQuery = true)
    List<Object[]> findNextDayAlarms();

    @Query(value = """
            select distinct mc.user_id, mc.intake_date, mc.intake_time 
            from schedule sc 
            join medicine_schedule mc ON mc.schedule_id = sc.id 
            join users u ON u.id = mc.user_id 
            where mc.eat_check = false 
                and sc.is_alarm = true 
                and mc.intake_date BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY) 
                and u.alarm_info = true 
                and mc.user_id= :userId
            """, nativeQuery = true)
    List<Object[]> findNextDayAlarmsById(@Param("userId") Long userId);
}

