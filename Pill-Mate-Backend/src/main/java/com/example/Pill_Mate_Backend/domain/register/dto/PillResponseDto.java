package com.example.Pill_Mate_Backend.domain.register.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "PillResponseDto", description = "약물 정보 응답 DTO")
public class PillResponseDto {

    @Schema(description = "식별 번호", example = "200903456")
    private Long itemSeq;

    @Schema(description = "약물 이름", example = "타이레놀정 500mg")
    private String itemName;

    @Schema(description = "분류명", example = "해열진통제")
    private String className;

    @Schema(description = "제조사명", example = "한국얀센")
    private String entpName;

    @Schema(description = "약물 이미지 URL", example = "https://example.com/images/tylenol.png")
    private String itemImage;
}
