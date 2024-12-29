package com.example.Pill_Mate_Backend.domain.check.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@Repository
public interface MedicineScheduleRepository2 extends JpaRepository<MedicineSchedule, Long> {
    @Query(value = "SELECT ms.id as medicinescheduleid, ms.intake_count as intakecount, ms.intake_time as intaketime, ms.eat_count as eatcount, " +
            "ms.eat_unit as eatunit, ms.meal_time as mealtime, ms.meal_unit as mealunit, ms.eat_check as eatcheck, m.medicine_name as medicinename, m.medicine_image as medicineimage " +
            "FROM medicine_schedule ms " +
            "JOIN medicine m " +
            "ON ms.medicine_id = m.id "+
            "JOIN users u " +
            "ON ms.user_id = u.id " +
            "WHERE ms.intake_date = :date AND u.email = :email "+
            "order by ms.intake_time"
            , nativeQuery = true)
    List<Object[]> findByIntakeDate(@Param("email") String email, @Param("date") Date date);

    @Query(value = "select m.medicine_name, m.medicine_image, m.class_name, m.ingredient, m.efficacy, m.caution, m.side_effect, m.storage " +
            "from medicine_schedule ms " +
            "join medicine m " +
            "on ms.medicine_id = m.id " +
            "where ms.id = :medicineScheduleId"
            , nativeQuery = true)
    Object[] findMedicineDetailByScheduleId(@Param("medicineScheduleId") long medicineScheduleId);

    //count 받아오기
    @Query(value = "Select count(*) From medicine_schedule ms " +
            "Join users u on u.id = ms.user_id " +
            "where ms.intake_date = :date and u.email = :email"
            , nativeQuery = true)
    Object[] findAllCountByDate(@Param("email") String email, @Param("date") Date date);
    @Query(value = "Select count(*) From medicine_schedule ms " +
            "Join users u on u.id = ms.user_id " +
            "where ms.intake_date = :date and u.email = :email and eat_check = 0"
            , nativeQuery = true)
    Object[] findLeftCountByDate(@Param("email") String email, @Param("date") Date date);

    //week 받아오기
    @Query(value = "Select date(ms.intake_date) as target " +
            "From medicine_schedule ms " +
            "Join users u on u.id = ms.user_id " +
            "where u.email = :email and ms.intake_date between :startdate and :enddate " +
            "group by date(ms.intake_date) " +
            "having sum(ms.eat_check) > 0 " +
            "order by target"
            , nativeQuery = true)
    List<Object[]> findExitWeekByDate(@Param("email") String email, @Param("startdate") Date startDate, @Param("enddate") Date endDate);
}