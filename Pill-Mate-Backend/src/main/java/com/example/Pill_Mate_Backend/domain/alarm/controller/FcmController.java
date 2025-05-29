package com.example.Pill_Mate_Backend.domain.alarm.controller;

import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.domain.alarm.dto.FcmRequestDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationIdDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationTitleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.service.FcmService;
import com.example.Pill_Mate_Backend.domain.alarm.service.NotificationService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")

public class FcmController {
    private final FcmService fcmService;
    @Autowired
    private final NotificationService notificationService;

    // 1. client가 server로 알림 생성 요청
    @PostMapping("/pushMessage")
    public ApiResponse<String> pushMessage(@RequestBody FcmRequestDTO requestDTO) throws IOException {
        System.out.println(requestDTO.getDeviceToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());
        fcmService.sendMessageTo(
                requestDTO.getDeviceToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ApiResponse.onSuccess("FCM_SEND_SUCCESS");//SuccessCode.FCM_SEND_SUCCESS, "fcm alarm success");
    }

    @PostMapping("/send")
    public String sendFcm(@RequestParam String token, @RequestParam String title, @RequestParam String body) {
        try {
            fcmService.sendMessageTo(token, title, body);
            return "✅ FCM 알림 전송 성공!";
        } catch (IOException e) {
            e.printStackTrace();
            return "❌ FCM 알림 전송 실패: " + e.getMessage();
        }
    }

    @GetMapping("/notification")
    public List<NotificationTitleDTO> sendAllNotification(){
        System.out.println("공지 전체 내용 전송 완료");
        return notificationService.getAllNotification();
    }

    @PostMapping("/notificationDetail")
    public NotificationDTO sendNotificationDetail(@RequestBody NotificationIdDTO notificationId){
        System.out.println("공지 디테일 전송 완료");
        return notificationService.getNotificationDetail(notificationId.getNotificationId());
    }
}
