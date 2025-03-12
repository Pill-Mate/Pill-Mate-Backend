package com.example.Pill_Mate_Backend.domain.alarm.controller;

import com.example.Pill_Mate_Backend.domain.alarm.dto.FcmRequestDTO;
import com.example.Pill_Mate_Backend.domain.alarm.service.FcmService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")

public class FcmController {
    private final FcmService fcmService;

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
}
