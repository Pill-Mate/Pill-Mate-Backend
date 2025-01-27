package com.example.Pill_Mate_Backend.domain.mypage.controller;

import com.example.Pill_Mate_Backend.domain.check.dto.MedicineCheckDTO;
import com.example.Pill_Mate_Backend.domain.check.dto.WeekCountDTO;
import com.example.Pill_Mate_Backend.domain.check.service.MedicineCheckService;
import com.example.Pill_Mate_Backend.domain.mypage.dto.AlarmDTO;
import com.example.Pill_Mate_Backend.domain.mypage.dto.RoutineDTO;
import com.example.Pill_Mate_Backend.domain.mypage.dto.UserInfoDTO;
import com.example.Pill_Mate_Backend.domain.mypage.service.AlarmService;
import com.example.Pill_Mate_Backend.domain.mypage.service.RoutineService;
import com.example.Pill_Mate_Backend.domain.mypage.service.UserInfoService;
import com.example.Pill_Mate_Backend.domain.oauth2.controller.AuthController;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MyPageController {
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private RoutineService routineService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AlarmService alarmService;

    @GetMapping("/userinforeturn")
    public UserInfoDTO getUserInfoData(@RequestHeader(value = "Authorization", required = true) String token) {
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
            }
        }
        UserInfoDTO userInfoDTO = userInfoService.getUserInfoByEmail(email);
        return userInfoDTO;
    }

    @GetMapping("/routinedata")
    public RoutineDTO getRoutineData(@RequestHeader(value = "Authorization", required = true) String token) {
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
            }
        }
        RoutineDTO routineDTO = routineService.getRoutineByEmail(email);

        return routineDTO;
    }

    @PatchMapping("/routineupdate")
    public ResponseEntity<?> routineUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody RoutineDTO routineDTO) {
        System.out.print(routineDTO);

        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
            }
        }

        if (routineDTO == null) {
            return ResponseEntity.badRequest().body("Invalid or empty request body");
        }

        try {
            routineService.routineUpdate(routineDTO, email);
            return ResponseEntity.ok("Routine updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/alarmdata")
    public AlarmDTO getAlarmData(@RequestHeader(value = "Authorization", required = true) String token) {
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
            }
        }
        AlarmDTO alarmDTO = alarmService.getAlarmByEmail(email);

        return alarmDTO;
    }

    @PatchMapping("/alarmupdate")
    public ResponseEntity<?> alarmUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody AlarmDTO alarmDTO) {
        System.out.print(alarmDTO);

        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                logger.info("email: "+email);
            } else {
                logger.info("Invalid JWT");
            }
        }

        if (alarmDTO == null) {
            return ResponseEntity.badRequest().body("Invalid or empty request body");
        }

        try {
            alarmService.alarmUpdate(alarmDTO, email);
            return ResponseEntity.ok("Alarm updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
