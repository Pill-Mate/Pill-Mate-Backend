package com.example.Pill_Mate_Backend.domain.mypage.service;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.check.service.MedicineCheckService;
import com.example.Pill_Mate_Backend.domain.mypage.dto.RoutineDTO;
import com.example.Pill_Mate_Backend.domain.mypage.dto.UserInfoDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RoutineService {
    @Autowired
    private UsersRepository usersRepository;
    public RoutineDTO getRoutineByEmail(String email) {
        List<Object[]> results = usersRepository.findRoutineByEmail(email);

        // 결과가 비어있는 경우 예외 던지기
        if (results.isEmpty()) {
            throw new RuntimeException("No user found with email: " + email);
        }

        Object[] result = results.get(0); // 첫 번째 행의 결과
        System.out.println("Result length: " + result.length);
        System.out.println("Result[0] type: " + result[0].getClass().getName());
        System.out.println("Result[1] type: " + result[1].getClass().getName());

        RoutineDTO routineDTO = new RoutineDTO(
                (Time) result[0],
                (Time) result[1],
                (Time) result[2],
                (Time) result[3],
                (Time) result[4]
        );

        return routineDTO;
    }

    @Transactional
    public void routineUpdate(RoutineDTO routineDTO, String email) {

        // 이메일로 유저 검색
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 루틴 업데이트
        users.setWakeupTime(routineDTO.getWakeupTime());
        users.setBedTime(routineDTO.getBedTime());
        users.setMorningTime(routineDTO.getMorningTime());
        users.setLunchTime(routineDTO.getLunchTime());
        users.setDinnerTime(routineDTO.getDinnerTime());

        // JPA가 @Transactional로 인해 자동으로 변경 사항을 감지하고 업데이트합니다.
    }
}
