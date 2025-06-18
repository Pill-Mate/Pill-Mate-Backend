package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.openApi.OpenapiHospital;
import com.example.Pill_Mate_Backend.domain.register.dto.HospitalResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpenapiHospitalRepository extends JpaRepository<OpenapiHospital, Long> {
    @Query("SELECT new com.example.Pill_Mate_Backend.domain.register.dto.HospitalResponseDTO(h.dutyAddr, h.dutyName, h.dutyTel) " +
            "FROM OpenapiHospital h WHERE h.dutyName LIKE %:dutyName%")
    List<HospitalResponseDTO> findByDutyNameContainingAsDto(@Param("dutyName") String dutyName);

}
