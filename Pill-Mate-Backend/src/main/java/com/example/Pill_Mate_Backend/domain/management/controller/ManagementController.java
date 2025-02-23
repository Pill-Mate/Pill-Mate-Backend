package com.example.Pill_Mate_Backend.domain.management.controller;

import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDto;
import com.example.Pill_Mate_Backend.domain.management.service.ManagementService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(("/api/v1/management"))
@RestController
public class ManagementController {
    private final ManagementService managementService;

    @Operation(summary="복용중인 약물 리스트",description = "복용중인 약물 리스트 조회")
    @GetMapping("/home/current")
    public EntityResponse<ApiResponse> currentHome(@AuthenticationPrincipal User user) {
        ManagementDto.CurrentPillResponseDto dto =  managementService.getCurrentList(user);
        return (EntityResponse<ApiResponse>) ApiResponse.onSuccess(dto);
    }

    @Operation(summary="복용중지한 약물 리스트",description = "복용중지한 약물 리스트 조회")
    @GetMapping("/home/stop")
    public EntityResponse<ApiResponse> stopHome(@AuthenticationPrincipal User user) {
        List<ManagementDto.CurrentPillResponse> dto =  managementService.getStopList(user);
        return (EntityResponse<ApiResponse>) ApiResponse.onSuccess(dto);
    }
}
