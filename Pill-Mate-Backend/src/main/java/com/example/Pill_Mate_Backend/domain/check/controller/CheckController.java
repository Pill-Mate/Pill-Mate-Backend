package com.example.Pill_Mate_Backend.domain.check.controller;

import com.example.Pill_Mate_Backend.domain.check.dto.*;
import com.example.Pill_Mate_Backend.domain.check.service.ClickMedicineService;
import com.example.Pill_Mate_Backend.domain.check.service.HomeService;
import com.example.Pill_Mate_Backend.domain.check.service.MedicineCheckService;
import com.example.Pill_Mate_Backend.domain.oauth2.controller.AuthController;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class CheckController {

    @Autowired
    private HomeService homeService;
    private final JwtService jwtService;
    private final MedicineCheckService medicineCheckService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private ClickMedicineService clickMedicineService;


    @PatchMapping("/medicinecheck")
    public ResponseEntity<?> updateMedicineCheck(@RequestBody List<MedicineCheckDTO> medicineCheckList) {
        System.out.print(medicineCheckList);
        if (medicineCheckList == null || medicineCheckList.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or empty request body");
        }

        medicineCheckService.updateCheckStatus(medicineCheckList);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/scheduledata")
    public ResponseDTO getMedicineSchedulesByDate(@RequestBody(required = false) ChangeDateDTO changeDateDTO, @RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                logger.info("Invalid JWT");
            }
        }

        Date mydate = new Date();
        if(changeDateDTO == null || changeDateDTO.getDate()==null || changeDateDTO.getDate().isEmpty()){
            String date =  format.format(mydate);
            mydate = format.parse(date); //date 안 넘겨줄 시 오늘로 date 설정
        }else{
            mydate = format.parse(changeDateDTO.getDate());
        }
        List<MedicineDTO> medicineList = homeService.getMedicineSchedulesByDate(email, mydate);
        WeekCountDTO weekCount = homeService.getWeekCountByDate(email, mydate);
        System.out.print(homeService.getMedicineSchedulesByDate(email, mydate));

        // Return response entity
        return ResponseDTO.builder()
                .medicineList(medicineList)
                .sunday(weekCount.getSunday())
                .monday(weekCount.getMonday())
                .tuesday(weekCount.getTuesday())
                .wednesday(weekCount.getWednesday())
                .thursday(weekCount.getThursday())
                .friday(weekCount.getFriday())
                .saturday(weekCount.getSaturday())
                .countAll(weekCount.getCountAll())
                .countLeft(weekCount.getCountLeft())
                .build();
    }

    @SneakyThrows
    @PostMapping("/weekscroll")
    public WeekDTO getWeekDateByDate(@RequestBody ChangeDateDTO changeDateDTO, @RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                logger.info("Invalid JWT");
            }
        }

        Date mydate = new Date();
        if(changeDateDTO == null || changeDateDTO.getDate()==null || changeDateDTO.getDate().isEmpty()){
            String date =  format.format(mydate);
            mydate = format.parse(date);
        }else{
            mydate = format.parse(changeDateDTO.getDate());
        }

        WeekDTO weekData = homeService.getWeekByDate(email, mydate);
        System.out.print(homeService.getWeekByDate(email, mydate));

        return weekData;
    }

    @PostMapping("/clickmedicine")
    public MedicineDetailDTO getMedicineDetail(@RequestBody ClickMedicineDTO clickMedicineDTO){
        System.out.print(clickMedicineDTO);
        return clickMedicineService.getMedicineDetailByScheduleId(clickMedicineDTO.getMedicineScheduleId());
    }
}
