package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Medicine findMedicineByIdentifyNumber(String identifyNumber); // 엔티티 반환
    List<Medicine> findAllByIdentifyNumber(String identifyNumber);

    @Query("""
    SELECT m
    FROM Medicine m
    JOIN m.users u
    JOIN Schedule s ON s.medicine = m
    WHERE m.identifyNumber = :identifyNumber
      AND u.email = :email
      AND s.status = 'ACTIVATE'
""")
    Optional<Medicine> findByIdentifyNumberAndEmail(@Param("identifyNumber") String identifyNumber,
                                                    @Param("email") String email);

    @Query("SELECT m.id FROM Medicine m WHERE m.identifyNumber = :identifyNumber")
    Long findMedicineIdByIdentifyNumber(@Param("identifyNumber") String identifyNumber); // ID 반환

    @Query("SELECT m FROM Medicine m WHERE m.users.email = :email")
    List<Medicine> findAllByEmail(@Param("email") String email);

    List<Medicine> findAllByIdentifyNumberIn(Collection<String> identifyNumbers);
    @Query("""
    SELECT m
    FROM Medicine m
    JOIN m.medicineSchedules ms
    JOIN ms.users u
    WHERE m.identifyNumber IN :identifyNumbers
      AND u.email = :email
""")
    List<Medicine> findAllByIdentifyNumberInAndUserEmail(@Param("identifyNumbers") Collection<String> identifyNumbers,
                                                         @Param("email") String email);

    @Query("SELECT m.medicineImage FROM Medicine m WHERE m.identifyNumber = :itemSeq")
    String findMedicineImageByItemSeq(@Param("itemSeq") String itemSeq);

    @Query("""
    SELECT m.id
    FROM Medicine m
    JOIN m.users u
    JOIN Schedule s ON s.medicine = m
    WHERE m.identifyNumber = :identifyNumber
      AND u.email = :email
      AND s.status = 'ACTIVATE'
""")
    Long findMedicineIdByIdentifyNumberAndEmail(@Param("identifyNumber")String itemSeq,@Param("email") String email);
}

