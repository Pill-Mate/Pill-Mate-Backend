package com.example.Pill_Mate_Backend.domain.register.service;

import com.example.Pill_Mate_Backend.CommonEntity.*;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeCount;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.register.dto.RegisterDTO;
import com.example.Pill_Mate_Backend.domain.register.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeCount.*;

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
        log.info("CreateMedicineSchedule: {}");
    }
    public Medicine CreateMedicine(RegisterDTO registerDTO,  Users users) {
        log.info("medicine1");
        Medicine medicine = Medicine.builder()
                //medicine
                .identifyNumber(registerDTO.identifyNumber())
                .medicineName(registerDTO.medicineName())
                .ingredient(registerDTO.ingredient())
                .medicineImage(registerDTO.medicineImage())
                .entpName(registerDTO.entpName())
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
                .mealUnit(registerDTO.mealUnit())
                .mealTime(registerDTO.mealTime())
                .eatUnit(registerDTO.eatUnit())
                .eatCount(registerDTO.eatCount())
                .medicineVolume(registerDTO.medicineVolume())
                .intakePeriod(registerDTO.intakePeriod())
                .intakeFrequencys(registerDTO.intakeFrequencys())
                .intakeCounts(registerDTO.intakeCounts())
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
                .hospitalAddress(registerDTO.hospitalAddress())
                .build();
        hospitalRepository.save(hospital);
    }
    public void CreatePharmacy(RegisterDTO registerDTO, Users users, Medicine medicine) {
        Pharmacy pharmacy = Pharmacy.builder()
                .users(users)
                .pharmacyName(registerDTO.pharmacyName())
                .pharmacyPhone(registerDTO.pharmacyPhone())
                .medicine(medicine)
                .pharmacyAddress(registerDTO.pharmacyAddress())
                .build();
        pharmacyRepository.save(pharmacy);
    }
    public void CreateMedicineSchedule(RegisterDTO registerDTO, Users users, Medicine medicine, Schedule schedule) {
        log.info("CreateMedicineSchedule1");
        //int intakeFrequency = registerDTO.intakeFrequency().size();  // Enum 개수 가져오기
        //int intakeCounts = registerDTO.intakeCounts().size();  // Enum 개수 가져오기
        List<MedicineSchedule> schedules = new ArrayList<>();      // 스케줄 저장 리스트

        // intakePeriod 동안 반복
        for (int i = 0; i < registerDTO.intakePeriod(); i++) {
            LocalDate currentDate = registerDTO.startDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().plusDays(i);  // 날짜 계산
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek(); //현재 날짜에 대한 요일
            log.info("CreateMedicineSchedule for문 1 i값:{} , registerDTO.intakePeriod():{} ",i,registerDTO.intakePeriod());
            if (registerDTO.intakeFrequencys().contains(dayOfWeek.toString())) {
                // 매일 Enum 개수만큼 MedicineSchedule 생성
                for (String intakeCount1 : registerDTO.intakeCounts()) {
                    IntakeCount intakeCount = IntakeCount.valueOf(intakeCount1); //String값 IntakeCount로 변환
                    log.info("CreateMedicineSchedule for문 2 value:{}", intakeCount.values());
                    // 섭취 시간을 계산하여 설정
                    LocalTime intakeTime = calculateIntakeTime(users, intakeCount, registerDTO.mealUnit(), registerDTO.mealTime());

                    MedicineSchedule medicineSchedule = MedicineSchedule.builder()
                            .medicine(medicine)
                            .users(schedule.getUsers())
                            .intakeDate(java.sql.Date.valueOf(currentDate))  // LocalDate -> sql Date 변환
                            .intakeTime(java.sql.Time.valueOf(intakeTime))   // 설정된 섭취 시간
                            .eatUnit(registerDTO.eatUnit())
                            .eatCount(registerDTO.eatCount())
                            .intakeCount(intakeCount)  // Enum 값 설정
                            .mealUnit(registerDTO.mealUnit())
                            .mealTime(registerDTO.mealTime())
                            .eatCheck(false)  // 초기값 false
                            .build();

                    schedules.add(medicineSchedule);  // 생성된 인스턴스를 리스트에 추가
                    log.info("medicineSchedule:{}", medicineSchedule);
                    medicineScheduleRepository.save(medicineSchedule);
                }
            }
        }

        // 생성된 스케줄 출력 (또는 저장)
        schedules.forEach(System.out::println);

    }

    // 섭취 시간 계산 로직
    private LocalTime calculateIntakeTime(Users users, IntakeCount intakeCount, MealUnit mealUnit, int mealTime) {
        log.info("calculateIntakeTime");
        LocalTime baseTime;

        // 섭취 시간 기준 설정
        switch (intakeCount) {
            case MORNING -> baseTime = users.getWakeupTime().toLocalTime();
            case LUNCH -> baseTime = users.getLunchTime().toLocalTime();
            case DINNER -> baseTime = users.getDinnerTime().toLocalTime();
            case EMPTY -> baseTime = users.getWakeupTime().toLocalTime();
            case SLEEP ->
            {
                //만약에 취침시간이 다음 날 오전 12시 이후, 즉 새벽일 경우에는
                if(users.getBedTime().toLocalTime().getHour() < 12) {
                    // 전날(당일 날) 오후 11시 50분으로 설정한다.
                    baseTime = LocalTime.of(23,50,0);
                }
                else {
                    baseTime = users.getBedTime().toLocalTime();
                }
            }
            case NEEDED -> baseTime = LocalTime.now();
            default -> throw new IllegalArgumentException("Invalid intake specific: " + intakeCount);
        }
        log.info("intakeFrequency");

        // 만약 취침 시간(SLEEP)이 당일 저녁 12시 이후, 즉 새벽이면 취췸 전 시간은 저녁 11시 50분으로 고정한다.
        if (users.getBedTime().toLocalTime().getHour() < 12) {

        }
        // MEALBEFORE / MEALAFTER에 따라 시간 조정
        if (mealUnit == MealUnit.MEALBEFORE && (intakeCount != EMPTY || intakeCount != SLEEP ||intakeCount != NEEDED )) {
            log.info("섭취 시간:{}",baseTime.minusMinutes(mealTime));
            return baseTime.minusMinutes(mealTime);  // 식전이면 시간 빼기
        } else if (mealUnit == MealUnit.MEALAFTER) {
            return baseTime.plusMinutes(mealTime);   // 식후면 시간 더하기
        } else if (mealUnit == null) {
            return baseTime;
        } else {
            throw new IllegalArgumentException("Invalid meal unit: " + mealUnit);
        }
    }

}
