package com.example.Pill_Mate_Backend.domain.mypage.service;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.mypage.dto.AlarmDTO;
import com.example.Pill_Mate_Backend.domain.mypage.dto.AlarmInfoDTO;
import com.example.Pill_Mate_Backend.domain.mypage.dto.AlarmMarketingDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AlarmService {
    @Autowired
    private UsersRepository usersRepository;
    /*
    public AlarmDTO getAlarmByEmail(String email) { //--삭제..
        List<Object[]> results = usersRepository.findAlarmByEmail(email);

        // 결과가 비어있는 경우 예외 던지기
        if (results.isEmpty()) {
            throw new RuntimeException("No user found with email: " + email);
        }

        Object[] result = results.get(0); // 첫 번째 행의 결과
        System.out.println("Result length: " + result.length);
        System.out.println("Result[0] type: " + result[0].getClass().getName());
        System.out.println("Result[1] type: " + result[1].getClass().getName());

        AlarmDTO alarmDTO = new AlarmDTO(
                (Boolean) result[0],
                (Boolean) result[1]
        );

        return alarmDTO;
    }

    @Transactional
    public void alarmUpdate(AlarmDTO alarmDTO, String email) { //--삭제

        // 이메일로 유저 검색
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 루틴 업데이트
        users.setAlarmMarketing(alarmDTO.getAlarmMarketing());
        users.setAlarmInfo(alarmDTO.getAlarmInfo());

        // JPA가 @Transactional로 인해 자동으로 변경 사항을 감지하고 업데이트합니다.
    }*/
    //
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
