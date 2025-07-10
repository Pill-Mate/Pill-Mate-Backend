package com.example.Pill_Mate_Backend.domain.alarm.controller;

import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.alarm.dto.*;
import com.example.Pill_Mate_Backend.domain.alarm.repository.FcmTokenRepository;
import com.example.Pill_Mate_Backend.domain.alarm.service.FcmService;
import com.example.Pill_Mate_Backend.domain.alarm.service.NotificationService;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final FcmTokenRepository fcmTokenRepository;

    @Autowired
    private final UsersRepository usersRepository;
    // 1. client가 server로 알림 생성 요청
    @PostMapping("/pushMessage")
    public ResponseEntity<ApiResponse<String>> pushMessage(@RequestBody FcmRequestDTO requestDTO) throws IOException {
        System.out.println(requestDTO.getDeviceToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());
        fcmService.sendMessageTo(
                requestDTO.getDeviceToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok(ApiResponse.onSuccess("FCM_SEND_SUCCESS"));//SuccessCode.FCM_SEND_SUCCESS, "fcm alarm success");
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendFcm(@RequestParam String token, @RequestParam String title, @RequestParam String body) {
        try {
            fcmService.sendMessageTo(token, title, body);
            return ResponseEntity.ok(ApiResponse.onSuccess("✅ FCM 알림 전송 성공!")) ;
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.onFailure("❌ FCM 알림 전송 실패: "+ e.getMessage() ));
        }
    }

    @GetMapping("/notification")
    public ResponseEntity<ApiResponse<List<NotificationTitleDTO>>> sendAllNotification(@RequestHeader(value = "Authorization", required = true) String token){
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                System.out.println("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        System.out.println("공지 전체 내용 전송 완료");
        return ResponseEntity.ok(ApiResponse.onSuccess(notificationService.getAllNotification(email)));
    }

    @PostMapping("/notificationDetail")
    public ResponseEntity<ApiResponse<NotificationDTO>> sendNotificationDetail(@RequestBody NotificationIdDTO notificationId, @RequestHeader(value = "Authorization", required = true) String token){
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                System.out.println("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        System.out.println("공지 디테일 전송 완료");
        return ResponseEntity.ok(ApiResponse.onSuccess(notificationService.getNotificationDetail(notificationId.getNotificationId(),email)));
    }
    @PostMapping("/registerFcmToken")
    public ResponseEntity<ApiResponse<String>> regesterFcmToken(@RequestBody RegisterFcmTokenDTO registerFcmTokenDTO, @RequestHeader(value = "Authorization", required = true) String token){
        String email;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                email = "";
                System.out.println("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        } else {
            email = "";
        }

        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        //fcmToken이 새거 일 시(새 디바이스로 로그인 했을 시)
        List<String> fcmTokens;
        fcmTokens = fcmTokenRepository.findFcmTokenByEmail((email));
        //토큰이 null이거나 같은 token이 내부에 없을 시
        if(fcmTokens==null || !fcmTokenRepository.existsByUsersAndFcmToken(users, registerFcmTokenDTO.getFcmToken())){
            System.out.println("fcm토큰 새로 등록");
            fcmService.registerToken(users, registerFcmTokenDTO.getFcmToken());
        }
        return ResponseEntity.ok(ApiResponse.onSuccess("FCM_REGISTER_SUCCESS"));
    }
}
