package com.example.Pill_Mate_Backend.domain.conflict.repository;

import com.example.Pill_Mate_Backend.CommonEntity.openApi.DurEffDuplication;
import com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DurEffDuplicationRepository extends JpaRepository<DurEffDuplication,Integer> {


    @Query("SELECT new com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDto(" +
            "t.itemName, t.itemSeq, t.className, t.effectName, t.entpName) " +
            "FROM DurEffDuplication t WHERE t.itemSeq = :itemSeq")
    List<EfcyDto> findEfcyByItemSeq(@Param("itemSeq") String itemSeq);

    @Query("SELECT new com.example.Pill_Mate_Backend.domain.conflict.dto.EfcyDto(" +
            "t.itemName, t.itemSeq, t.className, t.effectName, t.entpName) " +
            "FROM DurEffDuplication t WHERE t.durSeq = :durSeq")
    List<EfcyDto> findEfcyByDurSeq(@Param("durSeq") String durSeq);

    DurEffDuplication findByItemSeq(String itemSeq);

    @Query("SELECT e.itemSeq FROM DurEffDuplication e WHERE e.durSeq =:durSeq")
    List<String> findAllByDurSeq(@Param("durSeq") String durSeq);
}
