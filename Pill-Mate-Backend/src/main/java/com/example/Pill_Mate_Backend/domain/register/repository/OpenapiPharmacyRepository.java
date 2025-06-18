package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.openApi.OpenapiPharmacy;
import com.example.Pill_Mate_Backend.domain.register.dto.PharmacyResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpenapiPharmacyRepository extends JpaRepository<OpenapiPharmacy,Long> {
    @Query("SELECT new com.example.Pill_Mate_Backend.domain.register.dto.PharmacyResponseDTO(h.address, h.name, h.phone) " +
            "FROM OpenapiPharmacy h WHERE h.name LIKE %:dutyName%")
    List<PharmacyResponseDTO> findByDutyNameContainingAsDto(@Param("dutyName") String dutyName);
}
