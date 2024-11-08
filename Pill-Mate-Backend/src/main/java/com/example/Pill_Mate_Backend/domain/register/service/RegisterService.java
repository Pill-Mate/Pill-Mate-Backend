package com.example.Pill_Mate_Backend.domain.register.service;

import com.example.Pill_Mate_Backend.CommonEntity.*;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeSpecific;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.register.dto.RegisterDTO;
import com.example.Pill_Mate_Backend.domain.register.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.info;

@Slf4j
@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private PharmacyRepository pharmacyRepository;
    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public void Register(RegisterDTO registerDTO,  Users users) {
        log.info("Received DTO: {}", registerDTO);
        Medicine medicine = CreateMedicine(registerDTO, users);
        log.info("medicine: {}", medicine);
        CreateHospital(registerDTO, users, medicine);
        log.info(" CreateHospital: {}", registerDTO);
        CreatePharmacy(registerDTO, users, medicine);
        log.info("CreatePharmacy: {}", registerDTO);
        Schedule schedule = CreateSchedule(registerDTO, users, medicine);
        log.info("schedule: {}", registerDTO);
        CreateMedicineSchedule(registerDTO, users, medicine, schedule);
    }
    public Medicine CreateMedicine(RegisterDTO registerDTO,  Users users) {
        log.info("medicine1");
        Medicine medicine = Medicine.builder()
                //medicine
                .identifyNumber(registerDTO.identifyNumber())
                .medicineName(registerDTO.medicineName())
                .ingredient(registerDTO.ingredient())
                .medicineImage(registerDTO.medicineImage())
                .className(registerDTO.classname())
                .efficacy(registerDTO.efficacy())
                .sideEffect(registerDTO.sideEffect())
                .caution(registerDTO.caution())
                .storage(registerDTO.storage())
                .cautionTypes(registerDTO.cautionTypes())
                .ingredientUnit(registerDTO.ingredientUnit())
                .ingredientAmount(registerDTO.ingredientAmount())
                .users(users)
                .build();
        log.info("medicine2:{}",medicine);
        return medicineRepository.save(medicine);
    }
    public Schedule CreateSchedule(RegisterDTO registerDTO, Users users, Medicine medicine
    ) {
        Schedule schedule = Schedule.builder()
                //schedule
                .medicine(medicine)
                .intakeFrequency(registerDTO.intakeFrequency())
                .mealUnit(registerDTO.mealUnit())
                .mealTime(registerDTO.mealTime())
                .eatUnit(registerDTO.eatUnit())
                .eatCount(registerDTO.eatCount())
                .ingredientUnit(registerDTO.ingredientUnit())
                .medicineVolume(registerDTO.medicineVolume())
                .intakePeriod(registerDTO.intakePeriod())
                .intakeFrequency(registerDTO.intakeFrequency())
                .intakeCount(registerDTO.intakeCount())
                .isAlarm(registerDTO.isAlarm())
                .status(ScheduleStatus.ACTIVATE)
                .startDate(registerDTO.startDate())
                .build();
        return scheduleRepository.save(schedule);
    }
    public void CreateHospital(RegisterDTO registerDTO, Users users, Medicine medicine) {
        Hospital hospital = Hospital.builder()
                .users(users)
                .hospitalName(registerDTO.hospitalName())
                .hospitalPhone(registerDTO.hospitalPhone())
                .medicine(medicine)
                .build();
        hospitalRepository.save(hospital);
    }
    public void CreatePharmacy(RegisterDTO registerDTO, Users users, Medicine medicine) {
        Pharmacy pharmacy = Pharmacy.builder()
                .users(users)
                .pharmacyName(registerDTO.pharmacyName())
                .pharmacyPhone(registerDTO.pharmacyPhone())
                .medicine(medicine)
                .build();
        pharmacyRepository.save(pharmacy);
    }
    public void CreateMedicineSchedule(RegisterDTO registerDTO, Users users, Medicine medicine, Schedule schedule) {
        log.info("CreateMedicineSchedule");
        int intakeSpecificCount = IntakeSpecific.values().length;  // Enum 개수 가져오기
        List<MedicineSchedule> schedules = new ArrayList<>();      // 스케줄 저장 리스트

        // intakePeriod 동안 반복
        for (int i = 0; i < registerDTO.intakePeriod(); i++) {
            LocalDate currentDate = registerDTO.startDate().toInstant()
                   .atZone(ZoneId.systemDefault()).toLocalDate().plusDays(i);  // 날짜 계산

            // 매일 Enum 개수만큼 MedicineSchedule 생성
            for (IntakeSpecific intakeSpecific : IntakeSpecific.values()) {
                // 섭취 시간을 계산하여 설정
                LocalTime intakeTime = calculateIntakeTime(users, intakeSpecific, registerDTO.mealUnit(), registerDTO.mealTime());

                MedicineSchedule medicineSchedule = MedicineSchedule.builder()
                        .medicine(medicine)
                        .users(schedule.getUsers())
                        .intakeDate(java.sql.Date.valueOf(currentDate))  // LocalDate -> sql Date 변환
                        .intakeTime(java.sql.Time.valueOf(intakeTime))   // 설정된 섭취 시간
                        .eatUnit(registerDTO.eatUnit())
                        .eatCount(registerDTO.eatCount())
                        .intakeSpecific(intakeSpecific)  // Enum 값 설정
                        .mealUnit(registerDTO.mealUnit())
                        .mealTime(registerDTO.mealTime())
                        .eatCheck(false)  // 초기값 false
                        .build();

                schedules.add(medicineSchedule);  // 생성된 인스턴스를 리스트에 추가
                medicineScheduleRepository.save(medicineSchedule);
            }
        }

        // 생성된 스케줄 출력 (또는 저장)
        schedules.forEach(System.out::println);

    }

    // 섭취 시간 계산 로직
    private LocalTime calculateIntakeTime(Users users, IntakeSpecific intakeSpecific, MealUnit mealUnit, int mealTime) {
        log.info("calculateIntakeTime");
        LocalTime baseTime;

        // 섭취 시간 기준 설정
        switch (intakeSpecific) {
            case MORNING -> baseTime = users.getWakeupTime().toLocalTime();
            case LUNCH -> baseTime = users.getLunchTime().toLocalTime();
            case DINNER -> baseTime = users.getDinnerTime().toLocalTime();
            default -> throw new IllegalArgumentException("Invalid intake specific: " + intakeSpecific);
        }
        log.info("intakeSpecific");

        // MEALBEFORE / MEALAFTER에 따라 시간 조정
        if (mealUnit == MealUnit.MEALBEFORE) {
        log.info("섭취 시간:{}",baseTime.minusMinutes(mealTime));
            return baseTime.minusMinutes(mealTime);  // 식전이면 시간 빼기
        } else if (mealUnit == MealUnit.MEALAFTER) {
            return baseTime.plusMinutes(mealTime);   // 식후면 시간 더하기
        } else {
            throw new IllegalArgumentException("Invalid meal unit: " + mealUnit);
        }
    }

}
