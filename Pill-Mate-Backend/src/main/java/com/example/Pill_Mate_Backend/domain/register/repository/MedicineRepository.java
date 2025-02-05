package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Medicine findMedicineByIdentifyNumber(String identifyNumber); // 엔티티 반환
    @Query("SELECT m.id FROM Medicine m WHERE m.identifyNumber = :identifyNumber")
    Long findMedicineIdByIdentifyNumber(@Param("identifyNumber") String identifyNumber); // ID 반환
}

