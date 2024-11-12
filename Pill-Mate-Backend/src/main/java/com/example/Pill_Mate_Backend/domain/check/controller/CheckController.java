package com.example.Pill_Mate_Backend.domain.check.controller;

import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import com.example.Pill_Mate_Backend.domain.check.service.HomeService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class CheckController {

    @Autowired
    private HomeService homeService;
    private final JwtService jwtService;

    @GetMapping("/default")
    public List<MedicineDTO> getMedicineSchedulesByDate(@RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT");
            }
        }
        //LocalDate localDate = LocalDate.parse(date); // 문자열을 LocalDate로 변환 오늘 date 넘겨주기
        Date mydate = null;//----------------------!!!!!이거만 오늘로 바꾸기!!!!!!!
        try {
            mydate = format.parse("2024-11-03");
        } catch (ParseException e) {
            System.out.print("date문제");
            throw new RuntimeException(e);
        }
        System.out.print(homeService.getMedicineSchedulesByDate(email, mydate));
        return homeService.getMedicineSchedulesByDate(email, mydate);
    }
}
