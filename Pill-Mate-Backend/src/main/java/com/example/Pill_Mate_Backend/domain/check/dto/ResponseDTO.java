package com.example.Pill_Mate_Backend.domain.check.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseDTO {

    @Schema(description = "일요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean sunday;

    @Schema(description = "월요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean monday;

    @Schema(description = "화요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean tuesday;

    @Schema(description = "수요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean wednesday;

    @Schema(description = "목요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean thursday;

    @Schema(description = "금요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean friday;

    @Schema(description = "토요일 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean saturday;

    @Schema(description = "총 복용 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "8")
    private Integer countAll;

    @Schema(description = "남은 복용 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Integer countLeft;

    @Schema(description = "읽음 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean notificationRead;

    @Builder.Default
    @Schema(description = "약물 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MedicineDTO> medicineList;
}
