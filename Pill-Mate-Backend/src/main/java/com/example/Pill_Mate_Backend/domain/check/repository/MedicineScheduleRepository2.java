package com.example.Pill_Mate_Backend.domain.check.repository;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineScheduleRepository2 extends JpaRepository<MedicineSchedule, Long> {
    @Query(value = """
            SELECT 
                ms.id AS medicinescheduleid,
                ms.intake_count AS intakecount,
                ms.intake_time AS intaketime,
                ms.eat_count AS eatcount,
                ms.eat_unit AS eatunit,
                ms.meal_time AS mealtime,
                ms.meal_unit AS mealunit,
                ms.eat_check AS eatcheck,
                m.medicine_name AS medicinename,
                m.item_seq As itemseq,
                m.medicine_image AS medicineimage
            FROM medicine_schedule ms
            JOIN medicine m ON ms.medicine_id = m.id
            JOIN users u ON ms.user_id = u.id
            JOIN schedule s ON ms.schedule_id = s.id
            WHERE ms.intake_date = :date
              AND u.email = :email
              AND (
                    s.status = 'ACTIVATE'
                    OR (
                        s.status = 'INACTIVATE'
                        AND (
                            s.stopped_date IS NULL
                            OR TIMESTAMP(ms.intake_date, ms.intake_time) <= s.stopped_date
                            OR ms.eat_check = true
                        )
                    )
              )
            ORDER BY ms.intake_time
            """
            , nativeQuery = true)
    List<Object[]> findByIntakeDate(@Param("email") String email, @Param("date") LocalDate date);

    //schedule inactivate 시 stopped_date 이전 만 보이게.. 하지만 check 된건 보여야 한다..로 바꿈(위에 내용)
    /*"SELECT ms.id as medicinescheduleid, ms.intake_count as intakecount, ms.intake_time as intaketime, ms.eat_count as eatcount, " +
            "ms.eat_unit as eatunit, ms.meal_time as mealtime, ms.meal_unit as mealunit, ms.eat_check as eatcheck, m.medicine_name as medicinename, m.medicine_image as medicineimage " +
            "FROM medicine_schedule ms " +
            "JOIN medicine m " +
            "ON ms.medicine_id = m.id "+
            "JOIN users u " +
            "ON ms.user_id = u.id " +
            "WHERE ms.intake_date = :date AND u.email = :email "+
            "order by ms.intake_time"*/

    //
    /*SELECT
        ms.id AS medicinescheduleid,
        ms.intake_count AS intakecount,
        ms.intake_time AS intaketime,
        ms.eat_count AS eatcount,
        ms.eat_unit AS eatunit,
        ms.meal_time AS mealtime,
        ms.meal_unit AS mealunit,
        ms.eat_check AS eatcheck,
        m.medicine_name AS medicinename,
        m.medicine_image AS medicineimage
    FROM medicine_schedule ms
    JOIN medicine m ON ms.medicine_id = m.id
    JOIN users u ON ms.user_id = u.id
    JOIN schedule s ON ms.schedule_id = s.id
    WHERE ms.intake_date = '2025-05-30'
      AND u.email = 'kikidahee7@naver.com'
      AND s.status = 'ACTIVATE'  //----이부분 추가..
    ORDER BY ms.intake_time;*/

    @Query(value = "select m.medicine_name, m.medicine_image, m.class_name, m.ingredient, m.efficacy, m.caution, m.side_effect, m.storage, m.entp_name " +
            "from medicine_schedule ms " +
            "join medicine m " +
            "on ms.medicine_id = m.id " +
            "where ms.id = :medicineScheduleId"
            , nativeQuery = true)
    Object[] findMedicineDetailByScheduleId(@Param("medicineScheduleId") long medicineScheduleId);

    //count 받아오기
    /*"Select count(*) From medicine_schedule ms " +
            "Join users u on u.id = ms.user_id " +
            "where ms.intake_date = :date and u.email = :email"*/
    @Query(value =  """
                    SELECT COUNT(*)
                    FROM medicine_schedule ms
                    JOIN users u ON u.id = ms.user_id
                    JOIN schedule s ON ms.schedule_id = s.id
                    WHERE ms.intake_date = :date
                      AND u.email = :email
                      AND (
                        s.status = 'ACTIVATE'
                        OR (
                            s.status = 'INACTIVATE' AND (
                                TIMESTAMP(ms.intake_date, ms.intake_time) <= s.stopped_date
                                OR ms.eat_check = true
                            )
                        )
                      )
                    """
            , nativeQuery = true)
    Object[] findAllCountByDate(@Param("email") String email, @Param("date") LocalDate date);
    /*"Select count(*) From medicine_schedule ms " +
            "Join users u on u.id = ms.user_id " +
            "where ms.intake_date = :date and u.email = :email and eat_check = 0"*/
    @Query(value = """
                    SELECT COUNT(*)
                    FROM medicine_schedule ms
                    JOIN users u ON u.id = ms.user_id
                    JOIN schedule s ON ms.schedule_id = s.id
                    WHERE ms.intake_date = :date
                      AND u.email = :email
                      AND ms.eat_check = false
                      AND (
                        s.status = 'ACTIVATE'
                        OR (
                            s.status = 'INACTIVATE' AND (
                                TIMESTAMP(ms.intake_date, ms.intake_time) <= s.stopped_date
                                OR ms.eat_check = true
                            )
                        )
                      )
                    """
            , nativeQuery = true)
    Object[] findLeftCountByDate(@Param("email") String email, @Param("date") LocalDate date);

    //week 받아오기
    @Query(value = "Select date(ms.intake_date) as target " +
            "From medicine_schedule ms " +
            "Join users u on u.id = ms.user_id " +
            "where u.email = :email and ms.intake_date between :startdate and :enddate " +
            "group by date(ms.intake_date) " +
            "having sum(ms.eat_check) > 0 " +
            "order by target"
            , nativeQuery = true)
    List<Object[]> findExitWeekByDate(@Param("email") String email, @Param("startdate") LocalDate startDate, @Param("enddate") LocalDate endDate);

    //medicine_schedule id로 date 받아오기
    @Query(value = "select intake_date from medicine_schedule where id = :medicineScheduleId;"
            , nativeQuery = true)
    Object[] findDateByMedicineScheduleId(@Param("medicineScheduleId") long medicineScheduleId);

    @Query(value = """
        select a.item_image as medicineImage, 
               a.class_name as className, 
               a.item_name as medicineName, 
               a.entp_name as entpName, 
               b.type_name as caution, 
               c.use_method_qesitm as userMethod, 
               c.deposit_method as storage, 
               c.efcy_qesitm as efficacy, 
               c.atpn_qesitm as sideEffect
        from drug_basic a
        left join dur_product_info b on a.item_seq = b.item_seq
        left join drug_detail c on a.item_seq = c.item_seq
        where a.item_seq = :itemSeq
        """, nativeQuery = true)
    MedicineDetailDTO findMedicineDetailByItemSeq(@Param("itemSeq") long itemSeq);
}