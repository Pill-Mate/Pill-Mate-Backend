package com.example.Pill_Mate_Backend.domain.register.controller;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.alarm.service.FcmAlarmService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.register.dto.*;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.domain.register.service.RegisterService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/medicine")
@RequiredArgsConstructor
public class RegisterController {

    @Autowired
    RegisterService registerService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    private FcmAlarmService fcmAlarmService;

    @PostMapping("/test")
    public ApiResponse test(@RequestBody RegisterDTO registerDTO) {
        try {
            log.info(registerDTO.toString());
            return ApiResponse.onSuccess("약물등록 성공");
        } catch (Exception e) {
            //나중에 responseBody 추가
            return ApiResponse.onFailure("약물등록 실패");

        }
    }

    @Operation(summary = "약물등록", description = "사용자가 등록한 약물을 저장하는 api")
    @PostMapping("/register")
    public ApiResponse<Void> medicineRegister(@RequestHeader(value = "Authorization", required = true) String token,
                                           @RequestBody RegisterDTO registerDTO
                                           ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                log.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

            log.info("startDate:"+registerDTO.startDate().toString());

            Optional<Users> optionalUser = userRepository.findByEmail(email);
            Users user = optionalUser.get(); // Optional에서 값을 추출

//            Users users = Users.builder()
//                    .id(null)
//                    .profileImage(URI.create("https://example.com/profile.jpg"))  // 임시 프로필 이미지
//                    .username("testUser")  // 사용자 이름
//                    .email("testuser@example.com")  // 이메일
//                    .wakeupTime(Time.valueOf(LocalTime.of(7, 0)))  // 기상 시간 07:00
//                    .bedTime(Time.valueOf(LocalTime.of(23, 0)))  // 취침 시간 23:00
//                    .morningTime(Time.valueOf(LocalTime.of(8, 0)))  // 아침 08:00
//                    .lunchTime(Time.valueOf(LocalTime.of(12, 0)))  // 점심 12:00
//                    .dinnerTime(Time.valueOf(LocalTime.of(18, 0)))  // 저녁 18:00
//                    .alarmMarketing(true)  // 마케팅 알림 허용
//                    .alarmInfo(true)  // 정보 알림 허용
//                    .build(); //(임시) 로그인 연결 시 삭제

            //userRepository.save(user);
                //log.info(user.toString());
                registerService.Register(registerDTO, user);

            //알람 업데이트
            fcmAlarmService.resetAlarmTrigger(email);

            return ApiResponse.onSuccess(null);

    }

    @Operation(summary = "온보딩", description = "온보딩 시에 사용자의 아침, 점심, 저녁 설정 시간과 마케팅 알림 동의 여부 출력")
    @GetMapping("onboarding")
    public ApiResponse<OnboardingDTO> onboarding( @RequestHeader(value = "Authorization", required = true) String token
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                log.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }
            //토큰 가져올시에 이 코드로 수정
            Optional<Users> optionalUser = userRepository.findByEmail(email);
            Users users = optionalUser.get(); // Optional에서 값을 추출
            OnboardingDTO onboardingDTO = new OnboardingDTO(
                    users.getMorningTime(),
                    users.getLunchTime(),
                    users.getDinnerTime(),
                    users.getWakeupTime(),
                    users.getBedTime(),
                    users.getAlarmMarketing()
            );



            log.info(onboardingDTO.toString());
            //log.info(users.toString());

            return ApiResponse.onSuccess(onboardingDTO);

    }
    @Operation(summary = "약물 개수 확인" , description = "현재 날짜를 기준으로 복용중인 약물의 개수가 4개 이상인지 확인 -> 5개 부터 경고")
    @GetMapping("/pill-count-check")
    public ApiResponse<Boolean> getPillCounts( @RequestHeader(value = "Authorization", required = true) String token) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                log.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }

        }
        return ApiResponse.onSuccess(registerService.getPillCounts(email));
    }

    @Operation(summary = "약물 이름 검색", description = "약물을 이름으로(포함) 검색해서 보여줍니다.")
    @GetMapping("/search")
    public ApiResponse<List<PillResponseDto>> getPill (@RequestHeader(value = "Authorization", required = true) String token,
                                                       @RequestParam String itemName) {
        return ApiResponse.onSuccess(registerService.getPills(itemName));

    }

    @Operation(summary = "약국 이름 검색", description = "약국을 이름으로(포함) 검색해서 보여줍니다.")
    @GetMapping("/search/pharmacy")
    public ApiResponse<List<PharmacyResponseDTO>> getPharmacyList (@RequestHeader(value = "Authorization", required = true) String token,
                                                           @RequestParam String name) {
        return ApiResponse.onSuccess(registerService.getPharmacies(name));

    }

    @Operation(summary = "병원 이름 검색", description = "약국을 이름으로(포함) 검색해서 보여줍니다.")
    @GetMapping("/search/hospital")
    public ApiResponse<List<HospitalResponseDTO>> getHospitalList (@RequestHeader(value = "Authorization", required = true) String token,
                                                                   @RequestParam String name) {
        return ApiResponse.onSuccess(registerService.getHospitals(name));

    }


}
