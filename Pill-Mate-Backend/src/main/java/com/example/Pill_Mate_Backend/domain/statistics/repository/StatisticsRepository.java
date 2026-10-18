package com.example.Pill_Mate_Backend.domain.statistics.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticsRepository extends JpaRepository<MedicineSchedule, Long> {

    // 드롭다운용: 사용자의 전체 약물 스케줄(복용중 + 복용중지)
    @Query("SELECT s FROM Schedule s JOIN FETCH s.medicine m WHERE s.users.email = :email")
    List<Schedule> findSchedulesByEmail(@Param("email") String email);

    // 본인 약물인지 확인용
    @Query("SELECT s FROM Schedule s JOIN FETCH s.medicine m WHERE m.id = :medicineId AND s.users.email = :email")
    Optional<Schedule> findScheduleByMedicineIdAndEmail(@Param("medicineId") Long medicineId,
                                                        @Param("email") String email);

    @Query("SELECT ms FROM MedicineSchedule ms " +
            "WHERE ms.medicine.id = :medicineId AND ms.users.email = :email " +
            "AND ms.intakeDate BETWEEN :startDate AND :endDate " +
            "ORDER BY ms.intakeDate ASC")
    List<MedicineSchedule> findIntakesInPeriod(@Param("medicineId") Long medicineId,
                                               @Param("email") String email,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    // [연, 월, 약물 종류 수]
    @Query("SELECT YEAR(ms.intakeDate), MONTH(ms.intakeDate), COUNT(DISTINCT ms.medicine.id) " +
            "FROM MedicineSchedule ms " +
            "WHERE ms.users.email = :email AND ms.intakeDate BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(ms.intakeDate), MONTH(ms.intakeDate)")
    List<Object[]> countMedicineKindsByMonth(@Param("email") String email,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
}
