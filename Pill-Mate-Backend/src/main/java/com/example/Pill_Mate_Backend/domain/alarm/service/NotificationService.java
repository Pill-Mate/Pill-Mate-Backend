package com.example.Pill_Mate_Backend.domain.alarm.service;

import com.example.Pill_Mate_Backend.CommonEntity.Notification;
import com.example.Pill_Mate_Backend.domain.alarm.dto.AlarmScheduleDTO;
import com.example.Pill_Mate_Backend.domain.alarm.dto.NotificationDTO;
import com.example.Pill_Mate_Backend.domain.alarm.repository.NotificationRepository;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    public List<NotificationDTO> getAllNotification(){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> notificationDTOS = new ArrayList<> ();

        for(Notification noti : notifications){
            NotificationDTO dto = new NotificationDTO(
                    noti.getNotifyDate(),
                    noti.getNotifyTime(),
                    noti.getTitle(),
                    noti.getContent()
            );
            notificationDTOS.add(dto);
        }
        return notificationDTOS;
    }
}
