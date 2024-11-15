package com.example.Pill_Mate_Backend.domain.check.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {
    @Query(value = "SELECT ms.id as medicinescheduleid, ms.intake_specific as intakespecific, ms.intake_time as intaketime, ms.eat_count as eatcount, " +
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
}
