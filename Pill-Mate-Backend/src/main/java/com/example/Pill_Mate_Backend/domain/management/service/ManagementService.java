package com.example.Pill_Mate_Backend.domain.management.service;

import com.example.Pill_Mate_Backend.CommonEntity.Medicine;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeCount;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeFrequency;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDetailDto;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDto;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineScheduleRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.ScheduleRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.domain.register.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ManagementService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineScheduleRepository medicineScheduleRepository;
    private final RegisterService registerService;

    public ManagementDto.CurrentPillResponseDto getCurrentList(String email) {
            List<Schedule> schedules = scheduleRepository.findByUsersIdAndStatus(userRepository.findIdxByEmail(email).orElseThrow(), ScheduleStatus.ACTIVATE);
            List<ManagementDto.CurrentPillResponse>dto = schedules.stream().map(ManagementDto.CurrentPillResponse::from).collect(Collectors.toList());
            return ManagementDto.CurrentPillResponseDto.builder().pillCount(dto.size()).currentPillResponseList(dto).build();
    }

    public List<ManagementDto.StopPillResponse> getStopList(String email) {
        List<Schedule> schedules = scheduleRepository.findByUsersIdAndStatus(userRepository.findIdxByEmail(email).orElseThrow(), ScheduleStatus.INACTIVATE);
        return schedules.stream().map(ManagementDto.StopPillResponse::from).collect(Collectors.toList());
    }

    public void scheduleStop(String email, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        schedule.setStatus(ScheduleStatus.INACTIVATE);
        schedule.setStoppedDate(LocalDateTime.now());
        scheduleRepository.save(schedule);
    }

    public ManagementDetailDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        Users users = userRepository.findById(schedule.getUsers().getId()).orElseThrow();
        Medicine medicine = medicineRepository.findById(schedule.getMedicine().getId()).orElseThrow();
        List<LocalTime> intakeTimes = medicineScheduleRepository.findDistinctIntakeTimes(users.getId(), medicine.getId());
        //Set<LocalTime> intakeTimes = new LinkedHashSet<>(intakeTimesList);
        return ManagementDetailDto.builder()
                .itemSeq(medicine.getItemSeq())
                .medicineName(medicine.getMedicineName())
                .ingredient(medicine.getIngredient())
                .ingredientAmount(medicine.getIngredientAmount())
                .medicineImage(medicine.getMedicineImage())
                .entpName(medicine.getEntpName())
                .className(medicine.getClassName())
                .medicineId(medicine.getId())
                .wakeupTime(users.getWakeupTime())
                .morningTime(users.getMorningTime())
                .lunchTime(users.getLunchTime())
                .dinnerTime(users.getDinnerTime())
                .bedTime(users.getBedTime())
                .intakeCounts(
                        Arrays.stream(IntakeCount.values())
                                .filter(enumValue -> schedule.getIntakeCounts().contains(enumValue.name()))
                                .toList()
                )
                .intakeFrequencys(
                        Arrays.stream(IntakeFrequency.values())
                                .filter(enumValue -> schedule.getIntakeFrequencys().contains(enumValue.name()))
                                .toList()
                )

                .mealTime(schedule.getMealTime())
                .mealUnit(schedule.getMealUnit())
                .eatUnit(schedule.getEatUnit())
                .eatCount(schedule.getEatCount())
                .startDate(schedule.getStartDate()
                        .atTime(LocalTime.MIDNIGHT)
                        .atOffset(ZoneOffset.ofHours(9)))
                .intakePeriod(schedule.getIntakePeriod())
                .medicineVolume(schedule.getMedicineVolume())
                .ingredientUnit(schedule.getIngredientUnit())
                .isAlarm(schedule.getIsAlarm())
                .intakeTimes(intakeTimes)
                .build();
    }

    @Transactional
    public void modifyScheduleById(ManagementDetailDto dto,String email,Long scheduleId) {



        Medicine medicine = medicineRepository.findById(dto.medicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));


        medicine.setItemSeq(dto.itemSeq());
        medicine.setMedicineName(dto.medicineName());
        medicine.setIngredient(dto.ingredient());
        if (dto.ingredientAmount() != null) {
            medicine.setIngredientAmount(dto.ingredientAmount());
        }
        medicine.setMedicineImage(dto.medicineImage());
        medicine.setEntpName(dto.entpName());
        medicine.setClassName(dto.className());


        medicineRepository.save(medicine);

        Users user = userRepository.findByEmail(email).orElseThrow();
        user.setWakeupTime(dto.wakeupTime());
        user.setMorningTime(dto.morningTime());
        user.setLunchTime(dto.lunchTime());
        user.setDinnerTime(dto.dinnerTime());
        user.setBedTime(dto.bedTime());
        userRepository.save(user);


        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));


        schedule.setIntakeCounts(    dto.intakeCounts().stream()
                .map(Enum::name)
                .collect(Collectors.toSet()));
        schedule.setIntakeFrequencys(    dto.intakeFrequencys().stream()
                .map(Enum::name)
                .collect(Collectors.toSet()));
        schedule.setMealTime(dto.mealTime());
        schedule.setMealUnit(dto.mealUnit());
        schedule.setEatUnit(dto.eatUnit());
        schedule.setEatCount(dto.eatCount());
        schedule.setStartDate(dto.startDate()
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDate());
        schedule.setIntakePeriod(dto.intakePeriod());
        schedule.setMedicineVolume(dto.medicineVolume());
        schedule.setIngredientUnit(dto.ingredientUnit());
        schedule.setIsAlarm(dto.isAlarm());
        schedule.setUsers(user);
        schedule.setMedicine(medicine);


// 수정된 Schedule 객체 저장
        scheduleRepository.save(schedule);
        // ScheduleId에 해당하는 MedicineSchedule 먼저 삭제
        medicineScheduleRepository.deleteBySchedule(schedule);


        registerService.CreateMedicineSchedule(user, medicine, schedule);

        //Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        //Users users = userRepository.findById(schedule.getUsers().getId()).orElseThrow();
        //Medicine medicine = medicineRepository.findById(schedule.getMedicine().getId()).orElseThrow();
        //List<LocalTime> intakeTimesList = medicineScheduleRepository.findDistinctIntakeTimes(users.getId(), medicine.getId());
        //Set<LocalTime> intakeTimes = new LinkedHashSet<>(intakeTimesList);
//        return ManagementDetailDto.builder()
//
//
//
//                .intakeTimes(intakeTimes)
//                .build();
//    }
    }
}
