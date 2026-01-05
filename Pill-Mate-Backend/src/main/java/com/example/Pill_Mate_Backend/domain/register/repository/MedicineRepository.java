package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Medicine findMedicineByItemSeq(Long itemSeq); // 엔티티 반환
    List<Medicine> findAllByItemSeq(Long itemSeq);

    @Query("""
    SELECT m
    FROM Medicine m
    JOIN m.users u
    JOIN Schedule s ON s.medicine = m
    WHERE m.itemSeq = :itemSeq
      AND u.email = :email
      AND s.status = 'ACTIVATE'
""")
    Optional<Medicine> findByItemSeqAndEmail(@Param("itemSeq") Long itemSeq,
                                                    @Param("email") String email);

    @Query("SELECT m.id FROM Medicine m WHERE m.itemSeq = :itemSeq")
    Long findMedicineIdByItemSeq(@Param("itemSeq") Long itemSeq); // ID 반환

    @Query("SELECT m FROM Medicine m WHERE m.users.email = :email")
    List<Medicine> findAllByEmail(@Param("email") String email);

    List<Medicine> findAllByItemSeqIn(Collection<Long> itemSeq);
    @Query("""
    SELECT m
    FROM Medicine m
    JOIN m.medicineSchedules ms
    JOIN ms.users u
    WHERE m.itemSeq IN :itemSeq
      AND u.email = :email
""")
    List<Medicine> findAllByItemSeqInAndUserEmail(@Param("itemSeq") Set<Long> itemSeq,
                                                         @Param("email") String email);

    @Query("SELECT m.medicineImage FROM Medicine m WHERE m.itemSeq = :itemSeq")
    String findMedicineImageByItemSeq(@Param("itemSeq") Long itemSeq);

    @Query("""
    SELECT m.id
    FROM Medicine m
    JOIN m.users u
    JOIN Schedule s ON s.medicine = m
    WHERE m.itemSeq = :itemSeq
      AND u.email = :email
      AND s.status = 'ACTIVATE'
""")
    Long findMedicineIdByItemSeqAndEmail(@Param("itemSeq")Long itemSeq,@Param("email") String email);
}

