package com.example.Pill_Mate_Backend.domain.register.service;

import com.example.Pill_Mate_Backend.CommonEntity.*;
import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeCount;
import com.example.Pill_Mate_Backend.CommonEntity.enums.MealUnit;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.register.dto.HospitalResponseDTO;
import com.example.Pill_Mate_Backend.domain.register.dto.PharmacyResponseDTO;
import com.example.Pill_Mate_Backend.domain.register.dto.PillResponseDto;
import com.example.Pill_Mate_Backend.domain.register.dto.RegisterDTO;
import com.example.Pill_Mate_Backend.domain.register.repository.*;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.handler.UserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private DrugBasicRepository drugBasicRepository;
    @Autowired
    private OpenapiPharmacyRepository openapiPharmacyRepository;
    @Autowired
    private OpenapiHospitalRepository openapiHospitalRepository;


    public void Register(RegisterDTO registerDTO,  Users users) {
        log.info("Received DTO: {}", registerDTO);
        Medicine medicine = null;

        //만약에 이미 기존에 등록된 약물이 있으면 그 약물을 집어넣음
        if(medicineRepository.findMedicineByIdentifyNumber(registerDTO.identifyNumber()) != null){
            medicine = medicineRepository.findMedicineByIdentifyNumber(registerDTO.identifyNumber());
        } else {
            //만약에 새로운 약물이면 새로 생성
            medicine = CreateMedicine(registerDTO, users);
        }
        CreateHospital(registerDTO, users, medicine);
        CreatePharmacy(registerDTO, users, medicine);
        Schedule schedule = CreateSchedule(registerDTO, users, medicine);
        CreateMedicineSchedule(users, medicine, schedule);
    }
    public Medicine CreateMedicine(RegisterDTO registerDTO,  Users users) {
        log.info("medicine1");
        Medicine medicine = Medicine.builder()
                //medicine
                //약물 낱알 식별 번호
                .identifyNumber(registerDTO.identifyNumber())
                //약물이름
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
        //log.info("medicine2:{}",medicine);
        return medicineRepository.save(medicine);
    }
    public Schedule CreateSchedule(RegisterDTO registerDTO, Users users, Medicine medicine
    ) {
        Optional<Schedule> existingSchedule = scheduleRepository.findByUsersAndMedicineAndStatus(users, medicine);
        if (existingSchedule.isPresent()) {
            return existingSchedule.get();
        }
        log.info(registerDTO.startDate().toString());

        Schedule schedule = Schedule.builder()
                //schedule
                .medicine(medicine)
                .mealUnit(registerDTO.mealUnit())
                .mealTime(registerDTO.mealTime())
                .eatUnit(registerDTO.eatUnit())
                .eatCount(registerDTO.eatCount())
                .medicineVolume(registerDTO.medicineVolume())
                .intakePeriod(registerDTO.intakePeriod())
                .intakeFrequencys(registerDTO.intakeFrequencys().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .intakeCounts(registerDTO.intakeCounts().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .isAlarm(registerDTO.isAlarm())
                .status(ScheduleStatus.ACTIVATE)
                .startDate(registerDTO.startDate()
                        .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                        .toLocalDate())
                .users(users)
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
    public void CreateMedicineSchedule(Users users, Medicine medicine, Schedule schedule) {
        log.info("CreateMedicineSchedule1");
        List<MedicineSchedule> schedules = new ArrayList<>();      // 스케줄 저장 리스트

        // intakePeriod 동안 반복
        for (int i = 0; i < schedule.getIntakePeriod(); i++) {
            LocalDate currentDate = schedule.getStartDate().plusDays(i);  // 날짜 계산
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek(); //현재 날짜에 대한 요일
            log.info("CreateMedicineSchedule for문 1 i값:{} , schedule.intakePeriod():{} ",i,schedule.getIntakePeriod());
            if (schedule.getIntakeFrequencys().contains(dayOfWeek.name())) {
                // 매일 Enum 개수만큼 MedicineSchedule 생성
                for (String intakeCount1 : schedule.getIntakeCounts()) {
                    IntakeCount intakeCount = IntakeCount.valueOf(intakeCount1); //String값 IntakeCount로 변환
                    log.info("CreateMedicineSchedule for문 2 value:{}", intakeCount.values());
                    MedicineSchedule medicineSchedule = null;
                    // 섭취 시간을 계산하여 설정
                    if(intakeCount == IntakeCount.EMPTY || intakeCount == IntakeCount.SLEEP || intakeCount == IntakeCount.NEEDED) {
                        LocalTime intakeTime = calculateIntakeTime(users, intakeCount, null, schedule.getMealTime());

                        medicineSchedule = MedicineSchedule.builder()
                                .medicine(medicine)
                                .users(schedule.getUsers())
                                .intakeDate(currentDate)  // LocalDate -> sql Date 변환
                                .intakeTime(intakeTime)   // 설정된 섭취 시간
                                .eatUnit(schedule.getEatUnit())
                                .eatCount(schedule.getEatCount())
                                .intakeCount(intakeCount)  // Enum 값 설정
                                .mealUnit(null)
                                .mealTime(schedule.getMealTime())
                                .eatCheck(false)  // 초기값 false
                                .users(users)
                                .schedule(schedule)
                                .build();
                    }
                    else {
                        LocalTime intakeTime = calculateIntakeTime(users, intakeCount, schedule.getMealUnit(), schedule.getMealTime());

                        medicineSchedule = MedicineSchedule.builder()
                                .medicine(medicine)
                                .users(schedule.getUsers())
                                .intakeDate(currentDate)  // LocalDate -> sql Date 변환
                                .intakeTime(intakeTime)   // 설정된 섭취 시간
                                .eatUnit(schedule.getEatUnit())
                                .eatCount(schedule.getEatCount())
                                .intakeCount(intakeCount)  // Enum 값 설정
                                .mealUnit(schedule.getMealUnit())
                                .mealTime(schedule.getMealTime())
                                .eatCheck(false)  // 초기값 false
                                .users(users)
                                .schedule(schedule)
                                .build();
                    }
                    schedules.add(medicineSchedule);  // 생성된 인스턴스를 리스트에 추가
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
            case MORNING -> baseTime = users.getWakeupTime();
            case LUNCH -> baseTime = users.getLunchTime();
            case DINNER -> baseTime = users.getDinnerTime();
            case EMPTY -> baseTime = users.getWakeupTime();
            case SLEEP ->
            {
                //만약에 취침시간이 다음 날 오전 12시 이후, 즉 새벽일 경우에는
                if(users.getBedTime().getHour() < 12) {
                    // 전날(당일 날) 오후 11시 50분으로 설정한다.
                    baseTime = LocalTime.of(23,50,0);
                }
                else {
                    baseTime = users.getBedTime();
                }
            }
            case NEEDED -> baseTime = LocalTime.now();
            default -> throw new IllegalArgumentException("Invalid intake specific: " + intakeCount);
        }
        log.info("intakeFrequency");

        // MEALBEFORE / MEALAFTER에 따라 시간 조정
        if (mealUnit == MealUnit.MEALBEFORE && (intakeCount != EMPTY || intakeCount != SLEEP ||intakeCount != NEEDED )) {
            log.info("섭취 시간:{}",baseTime.minusMinutes(mealTime));
            return baseTime.minusMinutes(mealTime);  // 식전이면 시간 빼기
        } else if (mealUnit == MealUnit.MEALAFTER && (intakeCount != EMPTY || intakeCount != SLEEP ||intakeCount != NEEDED )) {
            return baseTime.plusMinutes(mealTime);   // 식후면 시간 더하기
        } else if (mealUnit == null) {
            return baseTime;
        } else {
            throw new IllegalArgumentException("Invalid meal unit: " + mealUnit);
        }
    }

    public boolean getPillCounts(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(()-> new UserHandler(ErrorStatus._NOT_FOUND_USER));
        Long pillCounts = scheduleRepository.countByUsersIdAndStatus(user.getId(), ScheduleStatus.ACTIVATE);
        if (pillCounts >= 4) {
            return false;
        }
        return true;
    }

    public List<PillResponseDto> getPills(String itemSeq) {
        return drugBasicRepository.findByItemNameContainingAsDto(itemSeq, PageRequest.of(0, 10)  // 0번째 페이지, 10개
        );
    }

    public List<HospitalResponseDTO> getHospitals(String name) {
        return openapiHospitalRepository.findByDutyNameContainingAsDto(name, PageRequest.of(0,10));
    }

    public List<PharmacyResponseDTO> getPharmacies(String name) {
        return openapiPharmacyRepository.findByDutyNameContainingAsDto(name,PageRequest.of(0,10));
    }
}
