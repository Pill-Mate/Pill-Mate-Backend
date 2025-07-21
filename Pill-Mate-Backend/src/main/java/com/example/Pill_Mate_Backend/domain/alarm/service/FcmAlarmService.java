package com.example.Pill_Mate_Backend.domain.alarm.service;


import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.CommonEntity.NotificationRead;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.repository.FcmTokenRepository;
import com.example.Pill_Mate_Backend.domain.alarm.repository.NotificationRepository;
import com.example.Pill_Mate_Backend.domain.alarm.repository.ScheduleRepository2;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


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
    private JwtService jwtService;
    @Autowired
    private final TaskScheduler taskScheduler;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    // userId별 현재 등록된 ScheduledFuture 리스트 관리<약물 시간 알람>
    private final Map<Long, List<ScheduledFuture<?>>> userScheduledTasks = new ConcurrentHashMap<>();

    //<약물 시간 알람>
    // 알람 등록 (여러 시간)
    public void scheduleAlarms(Long userId, List<LocalDateTime> intakeTimes) {
        //Long userId = (Long)usersRepository.getIdByEmail(email)[0];
        // 기존 알람 취소
        cancelAlarms(userId);

        List<ScheduledFuture<?>> futures = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (LocalDateTime time : intakeTimes) {
            if (time.isAfter(now)) { // 이미 지난 시간은 무시
                ScheduledFuture<?> future = taskScheduler.schedule(
                        () -> {
                            try {
                                sendAlarm(userId);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        Date.from(time.atZone(ZoneId.systemDefault()).toInstant())
                );
                futures.add(future);
                //예약된 알람 출력
                System.out.println("[알람 예약] Id: " +userId+" Time: "+time);
            }
        }

        // 새로운 알람 등록
        if (!futures.isEmpty()) {
            userScheduledTasks.put(userId, futures);
        }
    }

    // 기존 알람 취소
    public void cancelAlarms(Long userId) {
        List<ScheduledFuture<?>> futures = userScheduledTasks.get(userId);
        if (futures != null) {
            for (ScheduledFuture<?> future : futures) {
                future.cancel(false);
            }
            userScheduledTasks.remove(userId);
        }
    }

    // 알람 발송
    private void sendAlarm(Long userId) throws IOException {
        String title = "약 드실 시간이에요💊";
        String body = "잊지 말고 복약하세요!";
        System.out.println("💊 약 복용 시간!: " + userId);
        List<String> userFcmTokens = fcmTokenRepository.findActiveTokensByUserId(userId);
        if (userFcmTokens != null && !userFcmTokens.isEmpty()) {
            for (String token : userFcmTokens) {
                fcmService.sendMessageTo(token, title, body);
            }
        }
        //fcmService.sendMessageTo(fcmService.getFcmTokenById(userId),title, body);
    }

    // 이벤트 발생 시 특정 사용자 알람 재설정
    @Transactional
    public void resetAlarmTrigger(String email) {
        System.out.println("\n[알람 수정 시작됨] 이메일: " + email);

        // 1. 사용자 ID 조회 (예외 방어)
        Object[] idResult = usersRepository.getIdByEmail(email);
        if (idResult == null || idResult.length == 0 || idResult[0] == null) {
            throw new RuntimeException("해당 이메일에 대한 사용자 ID를 찾을 수 없습니다: " + email);
        }
        Long userId;
        try {
            userId = (Long) idResult[0];
        } catch (ClassCastException e) {
            throw new RuntimeException("userId를 Long으로 변환할 수 없습니다. 반환값: " + idResult[0]);
        }

        // 2. 알람 일정 조회
        List<Object[]> userAlarmsObject = scheduleRepository2.findNextDayAlarmsById(userId);
        if (Objects.isNull(userAlarmsObject) || userAlarmsObject.isEmpty()) {//userAlarmsObject == null || userAlarmsObject.isEmpty()) {
            System.out.println("현재 등록할 알람이 없습니다.");
            cancelAlarms(userId);
            return;
        }

        // 3. DTO 매핑
        List<AlarmScheduleDTO> userAlarms = new ArrayList<>();
        System.out.println("조회된 알람 데이터 수: " + userAlarmsObject.size());
        for (Object[] object : userAlarmsObject) {
            System.out.println("알람 raw 데이터: " + Arrays.toString(object));
            try {
                if (object[1] == null || object[2] == null) {
                    System.out.println("⚠️ null 값이 포함된 알람 데이터 건너뜀: " + Arrays.toString(object));
                    continue; // null date/time 방어 처리
                }

                AlarmScheduleDTO dto = new AlarmScheduleDTO(
                        (Long) object[0],
                        toLocalDate((Date) object[1]),
                        toLocalTime((Time) object[2])
                );
                userAlarms.add(dto);
            } catch (Exception e) {
                System.out.println("❌ DTO 매핑 중 오류 발생: " + Arrays.toString(object));
                e.printStackTrace();
            }
        }

        // 4. LocalDateTime 리스트 생성
        List<LocalDateTime> intakeTimes = userAlarms.stream()
                .map(dto -> LocalDateTime.of(dto.getIntakeDate(), dto.getIntakeTime()))
                .collect(Collectors.toList());

        // 5. 알람 등록
        scheduleAlarms(userId, intakeTimes);

        System.out.println("[✅ 알람 재등록 완료] User ID: " + userId + ", 등록된 알람 수: " + intakeTimes.size());
    }

    //
    @Scheduled(cron = "0 50 23 * * ?")//(cron = "0 59 23 * * ?") // 매일 11:59 PM
    public void prepareNextDayAlarms() {
        List<Object[]> alarmsObject = scheduleRepository2.findNextDayAlarms();
        List<AlarmScheduleDTO> alarms = new ArrayList<>();

        if (alarmsObject.isEmpty()) {
            throw new RuntimeException("alarm data not found");
        }

        //object DTO로 mapping
        for (Object[] object : alarmsObject){
            AlarmScheduleDTO dto = new AlarmScheduleDTO(
                    (Long) object[0],
                    toLocalDate((Date) object[1]),
                    toLocalTime((Time) object[2])
            );
            alarms.add(dto);
        }

        // userId별로 intakeTimes 묶기
        Map<Long, List<LocalDateTime>> userAlarmMap = new HashMap<>();
        for (AlarmScheduleDTO dto : alarms) {
            LocalDateTime time = LocalDateTime.of(dto.getIntakeDate(), dto.getIntakeTime());
            userAlarmMap.computeIfAbsent(dto.getUserId(), k -> new ArrayList<>()).add(time);
        }

        for (Map.Entry<Long, List<LocalDateTime>> entry : userAlarmMap.entrySet()) {
            scheduleAlarms(Long.valueOf(entry.getKey()), entry.getValue());
        }
    }

    //서버 재시작 시 알람 다시 예약
    @PostConstruct
    public void restoreScheduledAlarms() {
        List<Object[]> alarmsObject = scheduleRepository2.findNextDayAlarms();
        List<AlarmScheduleDTO> alarms = new ArrayList<>();

        if (alarmsObject.isEmpty()) {
            //throw new RuntimeException("alarm data not found");
            System.out.println("알람 대상 스케줄이 없습니다.(약물 복용 시간 알람)");
            return; // 아무 작업 없이 함수 종료
        }

        //object DTO로 mapping
        for (Object[] object : alarmsObject){
            AlarmScheduleDTO dto = new AlarmScheduleDTO(
                    (Long) object[0],
                    toLocalDate((Date) object[1]),
                    toLocalTime((Time) object[2])
            );
            alarms.add(dto);
        }

        // userId별로 intakeTimes 묶기
        Map<Long, List<LocalDateTime>> userAlarmMap = new HashMap<>();
        for (AlarmScheduleDTO dto : alarms) {
            LocalDateTime time = LocalDateTime.of(dto.getIntakeDate(), dto.getIntakeTime());
            userAlarmMap.computeIfAbsent(dto.getUserId(), k -> new ArrayList<>()).add(time);
        }

        for (Map.Entry<Long, List<LocalDateTime>> entry : userAlarmMap.entrySet()) {
            scheduleAlarms(Long.valueOf(entry.getKey()), entry.getValue());
        }
    }
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

    @Scheduled(cron = "0 0 14 * * ?")//@Scheduled(cron = "0 0 14 * * ?") // 매일 오후 2시에 실행
    public void endDateSendAlarms() throws IOException {
        System.out.println("복용 종료 알람 실행됨");

        Date today = new Date(); // 현재 날짜 (시간 포함)

        // isAlarm = true, status = ACTIVATE 인 스케줄 조회
        List<Object[]> schedules = scheduleRepository2.findByIsAlarmTrue();

        if (schedules.isEmpty()) {
            //throw new RuntimeException("schedule not found");
            System.out.println("알람 대상 스케줄이 없습니다.(복용 종료 알람)");
            return; // 아무 작업 없이 함수 종료
        }

        for (Object schedule : schedules) {
            Object[] innerArray = (Object[]) schedule;
            Date startDate = (Date) innerArray[0];
            int intakePeriod = (Integer) innerArray[1];
            long userId = (Long) innerArray[2];

            // 복용 마지막 날 계산
            Date endDate = addDays(startDate, intakePeriod);

            //
            //System.out.println("종료날:"+endDate);

            // 복용 마지막 날 기준 3일 전 계산
            Date alarmDate = addDays(endDate, -2);

            //System.out.println("알람날:"+alarmDate+" 오늘:"+today);
            // 오늘 날짜와 비교 (시간 제거)
            if (isSameDay(today, alarmDate)) {
                // 알림 메시지 생성
                String title = "복약 종료 알림";
                String body = String.format("'%s'의 복용이 3일 후 종료됩니다.", (String) innerArray[3]);
                System.out.println("알람 실행됨: "+body);

                //notification에 3일전 알람 데이터 넣기
                Notification notification = Notification.builder()
                        .notifyDate(LocalDate.now())
                        .notifyTime(LocalTime.parse("14:00:00"))
                        .userIdNoti(userId)
                        .title(title + " - " + body)
                        .content("FcmAlarm,no Content")
                        .build();
                notificationRepository.save(notification);

                // 사용자 FCM 토큰 가져오기
                /*
                String userFcmToken = fcmTokenRepository.findActiveTokenByUserId(userId);//schedule.getUsers().getFcmTokens()[0].getFcmToken();
                if (userFcmToken != null) {
                    //fcmService.sendNotification(userFcmToken, title, body);
                    fcmService.sendMessageTo(userFcmToken, title, body);  //fcm알람 보내기,,,,,,
                }*/
                //한 계정당 여러 기기 가능하게 변경.....
                List<String> userFcmTokens = fcmTokenRepository.findActiveTokensByUserId(userId);
                if (userFcmTokens != null && !userFcmTokens.isEmpty()) {
                    for (String token : userFcmTokens) {
                        fcmService.sendMessageTo(token, title, body);
                    }
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
    /*
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }*/
    private boolean isSameDay(Date date1, Date date2) {
        LocalDate d1 = toLocalDate(date1);
        LocalDate d2 = toLocalDate(date2);
        return d1.equals(d2);
    }

    //private LocalDate toLocalDate(Date date) {
    //    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    //}

    private LocalDate toLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime toLocalTime(Time time) {
        return time.toLocalTime(); // java.sql.Time → LocalTime
    }

    public void printScheduledTasks() {  //확인 필요시 사용
        System.out.println("===== [현재 등록된 알람 목록] =====");
        for (Map.Entry<Long, List<ScheduledFuture<?>>> entry : userScheduledTasks.entrySet()) {
            Long userId = entry.getKey();
            List<ScheduledFuture<?>> futures = entry.getValue();

            System.out.println("UserId: " + userId + " → 예약된 알람 수: " + futures.size());
        }
        System.out.println("==================================");
    }

}