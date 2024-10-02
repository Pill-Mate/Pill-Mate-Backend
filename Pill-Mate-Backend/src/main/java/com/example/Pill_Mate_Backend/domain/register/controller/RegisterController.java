package com.example.Pill_Mate_Backend.domain.register.controller;

import com.example.Pill_Mate_Backend.domain.register.service.RegisterService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medicine-regist")
@RequiredArgsConstructor
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @Operation(summary = "약물등록", description = "사용자가 등록한 약물을 저장하는 api")
    @GetMapping("/medicine-register")
    public ApiResponse<?> medicineRegister() {
        try {
            return ApiResponse.onSuccess("약물등록 성공");
        } catch (Exception e) {
            //나중에 responseBody 추가
            return ApiResponse.onFailure("약물등록 실패");

        }
    }

}
