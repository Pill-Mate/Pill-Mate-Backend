package com.example.Pill_Mate_Backend.domain.mypage.controller;

import com.example.Pill_Mate_Backend.domain.alarm.service.FcmAlarmService;
import com.example.Pill_Mate_Backend.domain.mypage.dto.*;
import com.example.Pill_Mate_Backend.domain.mypage.service.AlarmService;
import com.example.Pill_Mate_Backend.domain.mypage.service.MyPageService;
import com.example.Pill_Mate_Backend.domain.mypage.service.RoutineService;
import com.example.Pill_Mate_Backend.domain.oauth2.controller.AuthController;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MyPageController {
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private RoutineService routineService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private FcmAlarmService fcmAlarmService;

    @Operation(summary="마이페이지 정보", description = "마이페이지 정보 조회")
    @GetMapping("/mypagereturn")
    public ResponseEntity<ApiResponse<MyPageDTO>> getmyPageData(@RequestHeader(value = "Authorization", required = true) String token) {
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }
        MyPageDTO myPageDTO = myPageService.getMyPageByEmail(email);
        return ResponseEntity.ok(ApiResponse.onSuccess(myPageDTO));
    }

    @Operation(summary="개인 루틴 조회", description = "개인 루틴 데이터를 조회/전송")
    @GetMapping("/routinedata")
    public ResponseEntity<ApiResponse<RoutineDTO>> getRoutineData(@RequestHeader(value = "Authorization", required = true) String token) {
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }
        RoutineDTO routineDTO = routineService.getRoutineByEmail(email);

        return ResponseEntity.ok(ApiResponse.onSuccess(routineDTO)) ;
    }
    @Operation(summary="루틴 정보 수정", description = "개인 루틴 정보를 수정")
    @PatchMapping("/routineupdate")
    public ResponseEntity<ApiResponse<String>> routineUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody RoutineDTO routineDTO) {
        System.out.print(routineDTO);

        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        if (routineDTO == null) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure("Invalid or empty request body"));
        }

        try {
            routineService.routineUpdate(routineDTO, email);
            //알람 업데이트
            fcmAlarmService.resetAlarmTrigger(email);
            return ResponseEntity.ok(ApiResponse.onSuccess("Routine updated successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(e.getMessage()));
        }
    }
    @Operation(summary="미케팅 알람 수정", description = "마케팅 알람 정보 수정")
    @PatchMapping("/alarmupdate/marketing")
    public ResponseEntity<ApiResponse<String>> alarmMarketingUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody AlarmMarketingDTO alarmMarketingDTO) {
        System.out.print(alarmMarketingDTO);

        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        if (alarmMarketingDTO == null) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure("Invalid or empty request body"));
        }

        try {
            alarmService.alarmMarketingUpdate(alarmMarketingDTO, email);
            return ResponseEntity.ok(ApiResponse.onSuccess("Alarm updated successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(e.getMessage()));
        }
    }
    @Operation(summary="info알람 데이터 수정", description = "information 알람 데이터를 수정")
    @PatchMapping("/alarmupdate/information")
    public ResponseEntity<ApiResponse<String>> alarmInfoUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody AlarmInfoDTO alarmInfoDTO) {
        System.out.print(alarmInfoDTO);

        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        if (alarmInfoDTO == null) {
            return ResponseEntity.badRequest().body(ApiResponse.onSuccess("Invalid or empty request body"));
        }

        try {
            alarmService.alarmInfoUpdate(alarmInfoDTO, email);
            //알람 업데이트
            fcmAlarmService.resetAlarmTrigger(email);
            return ResponseEntity.ok(ApiResponse.onFailure("Alarm updated successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.onFailure(e.getMessage()));
        }
    }
}
