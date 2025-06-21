package com.example.Pill_Mate_Backend.domain.register.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;

@Getter
@Builder
@AllArgsConstructor
public class PillResponseDto {
    Long itemSeq;   // 식별 번호
    String itemName;  // 약물 이름
    String className; // 분류명
    String entpName;
    String itemImage;

}
