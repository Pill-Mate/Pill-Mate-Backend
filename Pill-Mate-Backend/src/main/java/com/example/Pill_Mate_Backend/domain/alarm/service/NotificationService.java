package com.example.Pill_Mate_Backend.domain.alarm.service;

import com.example.Pill_Mate_Backend.CommonEntity.FcmToken;
import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.CommonEntity.NotificationRead;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationTitleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.repository.NotificationReadRepository;
import com.example.Pill_Mate_Backend.domain.alarm.repository.NotificationRepository;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class NotificationService {

    @Autowired
    private NotificationReadRepository notificationReadRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UsersRepository usersRepository;
    public List<NotificationTitleDTO> getAllNotification(String email){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationTitleDTO> notificationTitleDTOS = new ArrayList<> ();

        for(Notification noti : notifications){
            //noti의 userIdNoti가 0(공지)가 아닐때 true.
            boolean isFcm = false;
            if (noti.getUserIdNoti() != 0)
                isFcm = true;

            NotificationTitleDTO dto = new NotificationTitleDTO(
                    noti.getId(),
                    noti.getNotifyDate(),
                    noti.getNotifyTime(),
                    noti.getTitle(),
                    notificationReadRepository.existsByNotificationIdAndUsersId(noti.getId(), (Long)usersRepository.getIdByEmail(email)[0]),
                    isFcm
            );
            notificationTitleDTOS.add(dto);
        }
        return notificationTitleDTOS;
    }

    public NotificationDTO getNotificationDetail(Long id, String email) {
        Notification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 존재하지 않습니다."));

        // 기존 읽음 기록 있는지 확인
        Optional<NotificationRead> existingRead = notificationReadRepository
                .findByNotificationIdAndUsersId(id, user.getId());

        if (existingRead.isPresent()) {
            // 이미 읽은 기록이 있으면 readAt만 업데이트
            NotificationRead read = existingRead.get();
            read.setReadAt(LocalDateTime.now());
            notificationReadRepository.save(read);
        } else {
            // 없으면 새로 생성
            NotificationRead notificationRead = NotificationRead.builder()
                    .users(user)
                    .notification(noti)
                    .readAt(LocalDateTime.now())
                    .build();
            notificationReadRepository.save(notificationRead);
        }

        return new NotificationDTO(
                noti.getNotifyDate(),
                noti.getNotifyTime(),
                noti.getTitle(),
                noti.getContent()
        );
    }

    public boolean getNotificationRead(String email){
        Long countUnread = notificationRepository.countUnreadByUserId(email);
        boolean result;
        if  (countUnread>0) result = false; //안 읽은게 있음
        else result = true; //다 읽음
        return result;
    }
}
