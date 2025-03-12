package com.example.Pill_Mate_Backend.domain.alarm.service;


import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.domain.alarm.repository.FcmTokenRepository;
import com.example.Pill_Mate_Backend.domain.alarm.repository.ScheduleRepository2;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAlarmService {

    @Autowired
    private ScheduleRepository2 scheduleRepository2;

    @Autowired
    private FcmService fcmService;
    @Autowired
    private FcmTokenRepository fcmTokenRepository;
    @Autowired
    private final JwtService jwtService;

/*
    @Scheduled(cron = "0 0 14 * * ?") // 매일 오전 9시에 실행
    @Transactional
    public void sendAnniversaryNotifications() {
        LocalDate today = LocalDate.now();
        List<Pet> pets = petRepository.findAll();

        for (Pet pet : pets) {
            if (today.equals(LocalDate.parse(pet.getAnniversary()))) {
                Users user = pet.getUser();
                String token = user.getDeviceToken();
                String title = "Anniversary";
                String body = "오늘은 " + pet.getPetName() + "의 기일 입니다.";

                try {
                    fcmService.sendMessageTo(token, title, body); // 이렇게
                } catch (IOException e) {
                    log.error("Failed to send FCM notification", e);
                }
            }
        }
    }*/

    @Scheduled(cron = "0 0 14 * * ?") // 매일 오후 2시에 실행
    public void checkAndSendAlarms(@RequestHeader(value = "Authorization", required = true) String token) throws IOException {
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
                System.out.println("email: "+email);
            } else {
                System.out.println("Invalid JWT");
            }
        }

        Date today = new Date(); // 현재 날짜 (시간 포함)

        // isAlarm = true인 스케줄 조회
        List<Object[]> schedules = scheduleRepository2.findByIsAlarmTrue(email);

        if (schedules.isEmpty()) {
            throw new RuntimeException("No user found with email: " + email);
        }

        for (Object schedule : schedules) {
            Object[] innerArray = (Object[]) schedule;
            Date startDate = (Date) innerArray[0];
            int intakePeriod = (Integer) innerArray[1];

            // 복용 마지막 날 계산
            Date endDate = addDays(startDate, intakePeriod - 1);

            // 복용 마지막 날 기준 3일 전 계산
            Date alarmDate = addDays(endDate, -3);

            // 오늘 날짜와 비교 (시간 제거)
            if (isSameDay(today, alarmDate)) {
                // 알림 메시지 생성
                String title = "복약 종료 알림";
                String body = String.format("'%s'의 복용이 3일 후 종료됩니다.", (String) innerArray[3]);

                // 사용자 FCM 토큰 가져오기
                String userFcmToken = fcmTokenRepository.findActiveTokenByUserId((Long) innerArray[2]);//schedule.getUsers().getFcmTokens()[0].getFcmToken();
                if (userFcmToken != null) {
                    //fcmService.sendNotification(userFcmToken, title, body);
                    fcmService.sendMessageTo(userFcmToken, title, body);  //fcm알람 보내기,,,,,,
                }
            }
        }
    }

    // 날짜 더하기/빼기 메서드
    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    // 날짜 비교 (시간 제거 후 같은 날짜인지 확인)
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}