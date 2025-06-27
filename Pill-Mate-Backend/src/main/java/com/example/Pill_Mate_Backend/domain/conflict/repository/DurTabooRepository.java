package com.example.Pill_Mate_Backend.domain.conflict.repository;

import com.example.Pill_Mate_Backend.CommonEntity.openApi.DurTaboo;
import com.example.Pill_Mate_Backend.domain.conflict.dto.TabooDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DurTabooRepository extends JpaRepository<DurTaboo, Integer> {
    @Query("SELECT m.mixtureItemSeq FROM DurTaboo m WHERE m.itemSeq = :itemSeq")
    List<String> findMixtureItemSeqByItemSeq(@Param("itemSeq") String itemSeq);


    @Query("SELECT new com.example.Pill_Mate_Backend.domain.conflict.dto.TabooDto(" +
            "t.mixItemName, t.mixtureItemSeq, t.prohbtContent, t.className, t.entpName) " +
            "FROM DurTaboo t WHERE t.itemSeq = :itemSeq")
    List<TabooDto> findTabooByMixtureItemSeq(@Param("itemSeq") String itemSeq);


}
