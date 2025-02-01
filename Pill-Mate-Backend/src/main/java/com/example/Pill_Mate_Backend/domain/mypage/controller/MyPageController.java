package com.example.Pill_Mate_Backend.domain.mypage.controller;

import com.example.Pill_Mate_Backend.domain.mypage.dto.*;
import com.example.Pill_Mate_Backend.domain.mypage.service.AlarmService;
import com.example.Pill_Mate_Backend.domain.mypage.service.MyPageService;
import com.example.Pill_Mate_Backend.domain.mypage.service.RoutineService;
import com.example.Pill_Mate_Backend.domain.oauth2.controller.AuthController;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
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

    @GetMapping("/mypagereturn")
    public MyPageDTO getmyPageData(@RequestHeader(value = "Authorization", required = true) String token) {
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
        MyPageDTO myPageDTO = myPageService.getMyPageByEmail(email);
        return myPageDTO;
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
    @PatchMapping("/alarmupdate/marketing")
    public ResponseEntity<?> alarmMarketingUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody AlarmMarketingDTO alarmMarketingDTO) {
        System.out.print(alarmMarketingDTO);

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

        if (alarmMarketingDTO == null) {
            return ResponseEntity.badRequest().body("Invalid or empty request body");
        }

        try {
            alarmService.alarmMarketingUpdate(alarmMarketingDTO, email);
            return ResponseEntity.ok("Alarm updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/alarmupdate/information")
    public ResponseEntity<?> alarmInfoUpdate(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody AlarmInfoDTO alarmInfoDTO) {
        System.out.print(alarmInfoDTO);

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

        if (alarmInfoDTO == null) {
            return ResponseEntity.badRequest().body("Invalid or empty request body");
        }

        try {
            alarmService.alarmInfoUpdate(alarmInfoDTO, email);
            return ResponseEntity.ok("Alarm updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
