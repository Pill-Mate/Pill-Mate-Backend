package com.example.Pill_Mate_Backend.domain.search.dto;

import com.example.Pill_Mate_Backend.domain.conflict.dto.AllConflictResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPillResponseDto {

    @Schema(description = "약물 이미지", example = "https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/1OjTeG8u8Kx")
    private String itemImage;

    @Schema(description = "약물 분류명", example ="기타의 소화기관용약")
    private String className;

    @Schema(description = "약물명", example ="아네모정")
    private String itemName;

    @Schema(description = "제약회사명", example ="삼진제약(주)")
    private String entpName;

    @Schema(description = "약물 충돌 정보")
    private AllConflictResponse allConflictResponse;

    @Schema(description = "사용법(복용법)", example = "성인은 1회 1/2~1정(125∼250 mg), 1일 3회 복용합니다.")
    private String useMethodQesitm;

    @Schema(description = "효능", example = "이 약은 근육통, 신경통, 외상(상처)후 및 수술후 통증, 두통, 치통, 귀통증의 경증(경증상, 가벼운 증상) 또는 중등도 통증의 완화에 사용합니다.")
    private String efcyQesitm;

    @Schema(description = "주의사항", example = "소화성궤양 환자, 약물에 기인하여 위·십이지장에 동요(안절부절) 발현, 이 약에 과민증 환자, 아스피린이나 다른 비스테로이드성 소염진통제(COX-2 억제제 포함)에 천식, 두드러기 또는 알레르기 반응 경험자, 관상동맥우회로술(CABG) 전후에 발생하는 통증의 치료 환자, 임부, 심한 간장애, 심한 신장애, 심한 심부전, 크론병 또는 궤양성 대장염과 같은 염증성 장질환 환자는 이 약을 복용하지 마십시오.이 약을 복용하기 전에 소화성궤양의 병력이 있는 환자, 소아 및 노인, 고혈압, 체액저류(체액 고임) 또는 심부전, 기관지천식 환자, 간장애 또는 경험자, 신장장애 또는 경험자, 수유부는 의사 또는 약사와 상의하십시오.")
    private String atpnQesitm;

    @Schema(description = "보관방법", example = "실온에서 보관하십시오.어린이의 손이 닿지 않는 곳에 보관하십시오.")
    private String depositMethod;
}
