package com.example.Pill_Mate_Backend.domain.alarm.service;

import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationTitleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.repository.NotificationRepository;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    public List<NotificationTitleDTO> getAllNotification(){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationTitleDTO> notificationTitleDTOS = new ArrayList<> ();

        for(Notification noti : notifications){
            NotificationTitleDTO dto = new NotificationTitleDTO(
                    noti.getId(),
                    noti.getNotifyDate(),
                    noti.getNotifyTime(),
                    noti.getTitle()
            );
            notificationTitleDTOS.add(dto);
        }
        return notificationTitleDTOS;
    }

    public NotificationDTO getNotificationDetail(Long id){
        Notification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        return new NotificationDTO(noti.getNotifyDate(), noti.getNotifyTime(), noti.getTitle(),noti.getContent());
    }
}
