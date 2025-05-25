package com.example.Pill_Mate_Backend.domain.alarm.service;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.alarm.dto.FcmMessage;
import com.example.Pill_Mate_Backend.domain.alarm.repository.FcmTokenRepository;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    @Value("${fcm.api.url}")
    private String API_URL;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FcmTokenRepository fcmTokenRepository;
    @Autowired
    private UsersRepository usersRepository;

    // 메시지를 구성하고 토큰을 받아서 FCM으로 메시지를 처리한다.
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, // 만든 message body에 넣기
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken()) // header에 포함
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        Response response = client.newCall(request).execute(); // 요청 보냄

        System.out.println(response.body().string());
    }

    // FCM 전송 정보를 기반으로 메시지를 구성한다. (Object -> String)
    private String makeMessage(String targetToken, String title, String body) throws com.fasterxml.jackson.core.JsonProcessingException  { // JsonParseException, JsonProcessingException
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                //.image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    // Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받는다.
    /*
    public String getAccessToken() throws IOException {
        final String firebaseConfigPath = "fcmAccountKey.json";//resources/fcmAccountKey.json";

        try {
            final GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            log.info("access token: {}",googleCredentials.getAccessToken());
            return googleCredentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            System.out.println("fcm IOException");
            throw new GeneralException(ErrorStatus.GOOGLE_REQUEST_TOKEN_ERROR);//"Failed to process Google request token", e);//ErrorCode.GOOGLE_REQUEST_TOKEN_ERROR);
        }
    }*/
    public String getAccessToken() throws IOException {
        try {
            String json = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
            if (json == null || json.isBlank()) {
                log.error("환경변수 GOOGLE_APPLICATION_CREDENTIALS_JSON 이 설정되어 있지 않습니다.");
                throw new GeneralException(ErrorStatus.GOOGLE_REQUEST_TOKEN_ERROR);
            }

            final GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            log.info("access token: {}", googleCredentials.getAccessToken());
            return googleCredentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            log.error("Firebase 토큰 발급 실패", e);
            throw new GeneralException(ErrorStatus.GOOGLE_REQUEST_TOKEN_ERROR);
        }
    }


    //chat

    public void registerToken(Users user, String token) {
        // 기존 토큰이 있다면 토큰 삭제
        //if(fcmTokenRepository.findActiveTokensByUserId(user.getId()) != null){
        //    fcmTokenRepository.deleteByUserId(user.getId());
        //}


        // 새 토큰 등록
        FcmToken fcmToken = FcmToken.builder()
                .users(user)
                .fcmToken(token)
                //.deviceType(deviceType)
                //.isActive(true)
                .build();
        //FcmToken fcmToken1 = new FcmToken(user.getId(), token);
        //Users users = new Users(userInfo.getName(), userInfo.getEmail(), userInfo.getProfileImage());
        // 데이터베이스에 사용자 정보 저장
        //userRepository.save(users);
        fcmTokenRepository.save(fcmToken);
    }

    public List<String> getFcmTokens(String email) {
        Optional<Users> users = usersRepository.findByEmail(email);
        List<String> tokenList =  fcmTokenRepository.findActiveTokensByUserId((Long)usersRepository.getIdByEmail(email)[0]);
        return tokenList;
    }
    public String getFcmTokenByEmail(String email) {
        Optional<Users> users = usersRepository.findByEmail(email);
        String tokenList =  fcmTokenRepository.findActiveTokenByUserId((Long)usersRepository.getIdByEmail(email)[0]);
        return tokenList;
    }
    public String getFcmTokenById(Long userId) {
        String tokenList =  fcmTokenRepository.findActiveTokenByUserId(userId);
        return tokenList;
    }
    /*
    public void sendNotification(String token, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(new com.google.firebase.messaging.Notification(title, body))
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}