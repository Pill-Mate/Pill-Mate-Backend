package com.example.Pill_Mate_Backend.domain.register.controller;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.register.dto.OnboardingDTO;
import com.example.Pill_Mate_Backend.domain.register.dto.RegisterDTO;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.domain.register.service.RegisterService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
    public ApiResponse<?> medicineRegister(//@AuthenticationPrincipal User user
                                           @RequestHeader(value = "Authorization", required = true) String token,
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
            }
        }

        try {
            Optional<Users> optionalUser = userRepository.findByEmail(email);
            Users user = optionalUser.get(); // Optional에서 값을 추출

            log.info(registerDTO.toString());
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
            return ApiResponse.onSuccess("약물등록 성공");
        } catch (Exception e) {
            //나중에 responseBody 추가
            return ApiResponse.onFailure("약물등록 실패");

        }
    }

    @Operation(summary = "온보딩", description = "온보딩 시에 사용자의 아침, 점심, 저녁 설정 시간과 마케팅 알림 동의 여부 출력")
    @GetMapping("onboarding")
    public ApiResponse<?> onboarding( @RequestHeader(value = "Authorization", required = true) String token
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                log.info("Invalid JWT");
            }
        }
        try {
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
        } catch (Exception e) {
            //나중에 responseBody 추가
            return ApiResponse.onFailure("사용자 시간 가져오기 실패");

        }
    }

}
