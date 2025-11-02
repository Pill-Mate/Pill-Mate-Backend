package com.example.Pill_Mate_Backend.domain.check.controller;

import com.example.Pill_Mate_Backend.domain.alarm.service.FcmAlarmService;
import com.example.Pill_Mate_Backend.domain.alarm.service.NotificationService;
import com.example.Pill_Mate_Backend.domain.check.dto.*;
import com.example.Pill_Mate_Backend.domain.check.service.ClickMedicineService;
import com.example.Pill_Mate_Backend.domain.check.service.HomeService;
import com.example.Pill_Mate_Backend.domain.check.service.MedicineCheckService;
import com.example.Pill_Mate_Backend.domain.oauth2.controller.AuthController;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import com.google.protobuf.Api;
import io.swagger.v3.oas.annotations.Operation;
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
    @Autowired
    private FcmAlarmService fcmAlarmService;
    @Autowired
    private NotificationService notificationService;
    @Operation(summary="약물 복용 체크 정보 수정", description = "medicine_schedule 약물 복용 체크시 정보 수정, 그 후 모든 당일 약물 리스트도 전송")
    @SneakyThrows
    @PatchMapping("/medicinecheck")
    public ResponseEntity<ApiResponse<ResponseDTO>> updateMedicineCheck(@RequestBody List<MedicineCheckDTO> medicineCheckList, @RequestHeader(value = "Authorization", required = true) String token) {
        System.out.println(medicineCheckList);
        if (medicineCheckList == null || medicineCheckList.isEmpty()) {
            logger.info("Invalid or empty request body");
            return null;
        }
        medicineCheckService.updateCheckStatus(medicineCheckList);
        System.out.println("오늘 db업데이트 완료!! : ");

        //schedule data 보내주기..
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }
        System.out.println("오늘 EMAIL!! : " + email);
        long medicineScheduleId = medicineCheckList.get(0).getMedicineScheduleId();

        Date mydate = homeService.getDateByScheduleId(medicineScheduleId);
        mydate = format.parse(String.valueOf(mydate));
        System.out.println("오늘 DATE!! : " + mydate);
        if (mydate == null) {
            mydate = new Date(); // 현재 날짜로 설정
            String date = format.format(mydate);
            mydate = format.parse(date);
        }

        List<MedicineDTO> medicineList = homeService.getMedicineSchedulesByDate(email, mydate);
        WeekCountDTO weekCount = homeService.getWeekCountByDate(email, mydate);
        System.out.print(homeService.getMedicineSchedulesByDate(email, mydate));

        //알람 업데이트
        if(medicineCheckService.findScheduleIsAfter(medicineScheduleId)==true){//medicineCheckList)==true){
            //알람 업데이트
            fcmAlarmService.resetAlarmTrigger(email);
        }

        //notificationread처리
        boolean notificationRead = notificationService.getNotificationRead(email);

        // Return response entity
        return ResponseEntity.ok(ApiResponse.onSuccess(ResponseDTO.builder()
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
                .notificationRead(notificationRead)
                .build()));
    }

    @Operation(summary="약물 체크 페이지 정보 전송", description = "특정 날짜 약물 체크 페이지 정보 전송-> 약물 리스트, 월-금 먹은 약물 유무")
    @SneakyThrows
    @PostMapping("/scheduledata")
    public ResponseEntity<ApiResponse<ResponseDTO>> getMedicineSchedulesByDate(@RequestBody(required = false) ChangeDateDTO changeDateDTO, @RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
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

        //notificationread처리
        boolean notificationRead = notificationService.getNotificationRead(email);

        // Return response entity
        return ResponseEntity.ok(ApiResponse.onSuccess(ResponseDTO.builder()
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
                .notificationRead(notificationRead)
                .build()));
    }
    @Operation(summary="주 스크롤", description = "일주일 스크롤 시 일주 후 체크페이지 정보 리스트 전송")
    @SneakyThrows
    @PostMapping("/weekscroll")
    public ResponseEntity<ApiResponse<WeekDTO>> getWeekDateByDate(@RequestBody ChangeDateDTO changeDateDTO, @RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                logger.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
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

        return ResponseEntity.ok(ApiResponse.onSuccess(weekData));
    }
    @Operation(summary="상세 정보", description = "약물 상세 정보 전송")
    @PostMapping("/medicinedetail")
    public ResponseEntity<ApiResponse<MedicineDetailDTO>> getMedicineDetail(@RequestBody ClickMedicineDTO clickMedicineDTO){
        System.out.print(clickMedicineDTO);
        return  ResponseEntity.ok(ApiResponse.onSuccess(clickMedicineService.getMedicineDetailByScheduleId(clickMedicineDTO.getItemSeq())));
    }
}
