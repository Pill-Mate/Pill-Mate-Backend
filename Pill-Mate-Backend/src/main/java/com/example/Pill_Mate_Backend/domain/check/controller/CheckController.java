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

    @SneakyThrows
    @GetMapping("/default")
    public List<MedicineDTO> getMedicineSchedulesByDate(@RequestHeader(value = "Authorization", required = true) String token) {
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

        Date mydate = format.parse("2024-11-03");//----------------------!!!!!이거만 오늘로 바꾸기!!!!!!!

        System.out.print(homeService.getMedicineSchedulesByDate(email, mydate));
        return homeService.getMedicineSchedulesByDate(email, mydate);
    }

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
    @PostMapping("/datechange2")
    public List<MedicineDTO> getMedicineSchedulesByDate2(@RequestBody ChangeDateDTO changeDateDTO, @RequestHeader(value = "Authorization", required = true) String token) {
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

        //date 받아서 데이터 뽑기.(homedefault와 함께 homeservice 사용..)
        Date mydate = format.parse(changeDateDTO.getDate());
        System.out.print(homeService.getMedicineSchedulesByDate(email, mydate));
        return homeService.getMedicineSchedulesByDate(email, mydate);
    }

    @SneakyThrows
    @PostMapping("/datechange")
    public ResponseDTO getMedicineSchedulesByDate3(@RequestBody ChangeDateDTO changeDateDTO, @RequestHeader(value = "Authorization", required = true) String token) {
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

        //date 받아서 데이터 뽑기.(homedefault와 함께 homeservice 사용..)
        Date mydate;
        if(changeDateDTO.getDate()==null || changeDateDTO.getDate()==""){
            mydate = format.parse("2024-11-03"); //null일때 오늘로 설정(바꿔야!-------------------------------------------
        }else{
            mydate = format.parse(changeDateDTO.getDate());
        }
        List<MedicineDTO> medicineList = homeService.getMedicineSchedulesByDate(email, mydate);
        WeekCountDTO weekCount = homeService.getWeekCountByDate(email, mydate);
        System.out.print(homeService.getMedicineSchedulesByDate(email, mydate));
        return new ResponseDTO(medicineList, weekCount);
    }

    @PostMapping("/clickmedicine")
    public MedicineDetailDTO getMedicineDetail(@RequestBody ClickMedicineDTO clickMedicineDTO){
        System.out.print(clickMedicineDTO);
        return clickMedicineService.getMedicineDetailByScheduleId(clickMedicineDTO.getMedicineScheduleId());
    }
}
