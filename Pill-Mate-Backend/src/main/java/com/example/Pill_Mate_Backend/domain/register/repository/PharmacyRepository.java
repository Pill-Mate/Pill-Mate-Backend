package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Pharmacy findByMedicineId(Long medicineId);
    Optional<Pharmacy> findById(Long id);
    Pharmacy findPharmaciesByPharmacyName(String PharmacyName);

}
