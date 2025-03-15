package com.example.Pill_Mate_Backend.domain.mypage.service;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.mypage.dto.RoutineDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.MedicineScheduleRepository3;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoutineService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MedicineScheduleRepository3 medicineScheduleRepository3;

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
                (LocalTime) result[0],//wake
                (LocalTime) result[1],//bed
                (LocalTime) result[2],//morning
                (LocalTime) result[3],//lunch
                (LocalTime) result[4]//dinner
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

        //medicine_schedule 테이블 수정
        //유저의 medicine_schedule 가져오기
        List<MedicineSchedule> schedules = medicineScheduleRepository3.findByUsers((Long)(usersRepository.getIdByEmail(email)[0]));

        for (MedicineSchedule schedule : schedules) {

            String intakeCount = String.valueOf(schedule.getIntakeCount()); // morning, lunch, dinner, sleep, empty
            String mealUnit = String.valueOf(schedule.getMealUnit()); // mealbefore, mealafter
            int mealTime = schedule.getMealTime(); // 분 단위

            LocalTime updatedTime = null;

            switch (intakeCount) {
                case "MORNING":
                    updatedTime = calculateTime(routineDTO.getMorningTime(), mealUnit, mealTime);
                    break;

                case "LUNCH":
                    updatedTime = calculateTime(routineDTO.getLunchTime(), mealUnit, mealTime);
                    break;

                case "DINNER":
                    updatedTime = calculateTime(routineDTO.getDinnerTime(), mealUnit, mealTime);
                    break;

                case "EMPTY":
                    updatedTime = routineDTO.getWakeupTime();
                    break;

                case "SLEEP":
                    updatedTime = calculateSleepTime(routineDTO.getBedTime());
                    break;

                default:
                    throw new RuntimeException("Invalid intake count: " + intakeCount);
            }

            // 3. 업데이트
            schedule.setIntakeTime(updatedTime);
            medicineScheduleRepository3.save(schedule);
        }

        // JPA가 @Transactional로 인해 자동으로 변경 사항을 감지하고 업데이트합니다.
    }
    // 시간 계산 함수
    private LocalTime calculateTime(LocalTime baseTime, String mealUnit, int mealTime) {
        if ("MEALBEFORE".equals(mealUnit)) {
            return baseTime.minusMinutes(mealTime);
        } else if ("MEALAFTER".equals(mealUnit)) {
            return baseTime.plusMinutes(mealTime);
        }
        throw new IllegalArgumentException("Invalid meal unit: " + mealUnit);
    }



    private LocalTime calculateSleepTime(LocalTime bedtime) {
        LocalTime elevenFifty = LocalTime.of(23, 50); // 23:50 (저녁 11시 50분)

        if (bedtime.isBefore(LocalTime.NOON)) { // 오전 12시(정오)보다 이른 시간인지 체크
            return elevenFifty;
        } else {
            return bedtime.minusMinutes(30); // 취침 시간에서 30분을 뺌
        }
    }

}
