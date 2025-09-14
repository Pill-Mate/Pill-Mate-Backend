package com.example.Pill_Mate_Backend.domain.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "MyPageDTO", description = " 마이페이지 조회")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {

    @Schema(description =  "알림 설정 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "userName은 필수 값입니다.")
    private String userName;
    private String email;
    private String profileImage;
    private Boolean alarmMarketing;
    private Boolean alarmInfo;
}
