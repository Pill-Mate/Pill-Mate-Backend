package com.example.Pill_Mate_Backend.domain.management.controller;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDetailDto;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDto;
import com.example.Pill_Mate_Backend.domain.management.service.ManagementService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(("/api/v1/management"))
@RestController
public class ManagementController {
    private final ManagementService managementService;
    private final JwtService jwtService;

    @Operation(summary="복용중인 약물 리스트",description = "복용중인 약물 리스트 조회")
    @GetMapping("/home/current")
    public ApiResponse<?> currentHome(@RequestHeader(value = "Authorization", required = true) String token) {

        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String email = "";
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                if (jwtService.validateToken(jwtToken)) {
                    email = jwtService.extractEmail(jwtToken);

                } else {
                    log.info("Invalid JWT");
                    throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
                }
            }
            ManagementDto.CurrentPillResponseDto dto = managementService.getCurrentList(email);
            return ApiResponse.onSuccess(dto);
        }
    }
    @Operation(summary="복용중지한 약물 리스트",description = "복용중지한 약물 리스트 조회")
    @GetMapping("/home/stop")
    public ApiResponse<?> stopHome(@RequestHeader(value = "Authorization", required = true)  String token) {
            {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String email = "";
                if (token != null && token.startsWith("Bearer ")) {
                    String jwtToken = token.substring(7);
                    if (jwtService.validateToken(jwtToken)) {
                        email = jwtService.extractEmail(jwtToken);

                    } else {
                        log.info("Invalid JWT");
                        throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
                    }
                }

        List<ManagementDto.StopPillResponse> dto =  managementService.getStopList(email);
        return ApiResponse.onSuccess(dto);
    }
}
    @Operation(summary="스케줄 복용 중지",description = "현재 복용중인 약물 스케줄을 복용 중지 처리 합니다. ")
    @PatchMapping("/home/current/{scheduleId}")
    public ApiResponse<?> stopHome(@RequestHeader(value = "Authorization", required = true)  String token,
                                   @PathVariable("scheduleId") Long scheduleId) {
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String email = "";
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                if (jwtService.validateToken(jwtToken)) {
                    email = jwtService.extractEmail(jwtToken);

                } else {
                    log.info("Invalid JWT");
                    throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
                }
            }

             managementService.sheduleStop(email,scheduleId);
            return ApiResponse.onSuccess(null);
        }
    }

    @Operation(summary = "약물 관리 수정페이지", description = "한 약물의 세부사항을 전송합니다.")
    @GetMapping("/detail/{scheduleId}")
    public ApiResponse<?> managementDetail(@PathVariable Long scheduleId) {
        ManagementDetailDto dto = managementService.findScheduleById(scheduleId);
        return ApiResponse.onSuccess(dto);

    }
    @Operation(summary = "약물 관리 수정페이지", description = "약물 관리 수정사항을 전송받고 수정합니다.")
    @PutMapping("/detail/{scheduleId}")
    public ApiResponse<?> managementDetailModify(
                                                 @PathVariable Long scheduleId,
                                                 @RequestBody ManagementDetailDto Reqdto,
                                                 @RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                log.info("Invalid JWT");
            }
        }
         managementService.modifyScheduleById(Reqdto,email,scheduleId);
        return ApiResponse.onSuccess("성공");

    }
    }
