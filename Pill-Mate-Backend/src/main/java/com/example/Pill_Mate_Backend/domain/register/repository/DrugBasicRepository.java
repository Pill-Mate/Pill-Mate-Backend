package com.example.Pill_Mate_Backend.domain.register.repository;

import com.example.Pill_Mate_Backend.CommonEntity.openApi.DrugBasic;
import com.example.Pill_Mate_Backend.domain.register.dto.PillResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugBasicRepository extends JpaRepository<DrugBasic, Long> {
    @Query("SELECT new com.example.Pill_Mate_Backend.domain.register.dto.PillResponseDto(d.itemSeq, d.itemName, d.className, d.entpName, d.itemImage) " +
            "FROM DrugBasic d WHERE d.itemName LIKE %:itemName%")
    List<PillResponseDto> findByItemNameContainingAsDto(@Param("itemName") String itemName, Pageable pageable);
}
