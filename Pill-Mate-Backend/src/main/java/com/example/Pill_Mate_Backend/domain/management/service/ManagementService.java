package com.example.Pill_Mate_Backend.domain.management.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDetailDto;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDto;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineScheduleRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.ScheduleRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ManagementService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineScheduleRepository medicineScheduleRepository;

    public ManagementDto.CurrentPillResponseDto getCurrentList(String email) {
            List<Schedule> schedules = scheduleRepository.findByUsersIdAndStatus(userRepository.findIdxByEmail(email).orElseThrow(), ScheduleStatus.ACTIVATE);
            List<ManagementDto.CurrentPillResponse>dto = schedules.stream().map(ManagementDto.CurrentPillResponse::from).collect(Collectors.toList());
            return ManagementDto.CurrentPillResponseDto.builder().pillCount(dto.size()).currentPillResponseList(dto).build();
    }

    public List<ManagementDto.StopPillResponse> getStopList(String email) {
        List<Schedule> schedules = scheduleRepository.findByUsersIdAndStatus(userRepository.findIdxByEmail(email).orElseThrow(), ScheduleStatus.INACTIVATE);
        return schedules.stream().map(ManagementDto.StopPillResponse::from).collect(Collectors.toList());
    }

    public void sheduleStop(String email, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        schedule.setStatus(ScheduleStatus.INACTIVATE);
        scheduleRepository.save(schedule);
    }

    public ManagementDetailDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        Users users = userRepository.findById(schedule.getUsers().getId()).orElseThrow();
        Medicine medicine = medicineRepository.findById(schedule.getMedicine().getId()).orElseThrow();
        List<LocalTime> intakeTimesList = medicineScheduleRepository.findDistinctIntakeTimes(users.getId(), medicine.getId());
        Set<LocalTime> intakeTimes = new HashSet<>(intakeTimesList);
        return ManagementDetailDto.builder()
                .identifyNumber(medicine.getIdentifyNumber())
                .medicineName(medicine.getMedicineName())
                .ingredient(medicine.getIngredient())
                .ingredientAmount(medicine.getIngredientAmount())
                .medicineImage(medicine.getMedicineImage())
                .entpName(medicine.getClassName())
                .className(medicine.getClassName())
                .medicineId(medicine.getId())
                .wakeupTime(users.getWakeupTime())
                .morningTime(users.getMorningTime())
                .lunchTime(users.getLunchTime())
                .dinnerTime(users.getDinnerTime())
                .bedTime(users.getBedTime())
                .intakeCounts(schedule.getIntakeCounts())
                .intakeFrequencys(schedule.getIntakeFrequencys())
                .mealTime(schedule.getMealTime())
                .mealUnit(schedule.getMealUnit())
                .eatUnit(schedule.getEatUnit())
                .eatCount(schedule.getEatCount())
                .startDate(schedule.getStartDate())
                .intakePeriod(schedule.getIntakePeriod())
                .medicineVolume(schedule.getMedicineVolume())
                .ingredientUnit(schedule.getIngredientUnit())
                .isAlarm(schedule.getIsAlarm())
                .intakeTimes(intakeTimes)
                .build();
    }
}
