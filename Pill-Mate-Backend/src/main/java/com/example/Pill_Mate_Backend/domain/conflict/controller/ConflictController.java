package com.example.Pill_Mate_Backend.domain.conflict.controller;


import com.example.Pill_Mate_Backend.domain.conflict.dto.MedicineConflict;
import com.example.Pill_Mate_Backend.domain.conflict.service.MedicineService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConflictController {
    private final MedicineService medicineService;
    private final JwtService jwtService;

    @Operation(summary = "중복 약물 검사", description = "등록하려는 약물을 이미 복용중인지  조회합니다.")
    @GetMapping("/check-duplicate-drug")
    public MedicineConflict getConflict(@RequestParam Long itemSeq,
                                        @RequestHeader(value = "Authorization", required = true)  String token) {
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

        return medicineService.findAll(itemSeq,email);
    }
}
