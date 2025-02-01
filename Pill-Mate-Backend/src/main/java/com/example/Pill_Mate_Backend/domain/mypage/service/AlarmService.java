package com.example.Pill_Mate_Backend.domain.mypage.service;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.mypage.dto.AlarmInfoDTO;
import com.example.Pill_Mate_Backend.domain.mypage.dto.AlarmMarketingDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AlarmService {
    @Autowired
    private UsersRepository usersRepository;
    @Transactional
    public void alarmMarketingUpdate(AlarmMarketingDTO alarmMarketingDTO, String email) {

        // 이메일로 유저 검색
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 루틴 업데이트
        users.setAlarmMarketing(alarmMarketingDTO.getAlarmMarketing());

        // JPA가 @Transactional로 인해 자동으로 변경 사항을 감지하고 업데이트합니다.
    }
    @Transactional
    public void alarmInfoUpdate(AlarmInfoDTO alarmInfoDTO, String email) {

        // 이메일로 유저 검색
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 루틴 업데이트
        users.setAlarmInfo(alarmInfoDTO.getAlarmInfo());

        // JPA가 @Transactional로 인해 자동으로 변경 사항을 감지하고 업데이트합니다.
    }
}
