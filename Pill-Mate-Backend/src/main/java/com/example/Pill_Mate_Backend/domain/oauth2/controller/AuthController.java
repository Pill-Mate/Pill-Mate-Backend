package com.example.Pill_Mate_Backend.domain.oauth2.controller;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.KakaoSignUpDTO;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.OnboardingDTO;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.UserInfoResponseDto;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.KakaoService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.OnboardingService;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final KakaoService kakaoService;
    private final OnboardingService onboardingService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    //로그 확인

    // 프론트에서 인가코드를 받으면 이 엔드포인트가 호출됨
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> kakaoLogin(
                                                          @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                          @RequestBody KakaoSignUpDTO kakaoSignUpDto,
                                                          HttpSession session) {

        // JWT가 존재하는 경우에만 처리
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            // JWT를 사용한 추가 처리 가능
        }
        // 본문에서 카카오 Access Token 가져오기
        String kakaoAccessToken = kakaoSignUpDto.getKakaoAccessToken();

        // 카카오 Access Token으로 사용자 정보 조회
        UserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);

        // 이메일로 유저가 이미 존재하는지 확인
        Optional<Users> existingUser = userRepository.findByEmail(userInfo.getEmail());
        if (existingUser.isPresent()) {
            // 이미 존재하는 유저 -> 로그인 처리
            Users users = existingUser.get();

            // 기존 유저 정보 업데이트
            users.setUsername(userInfo.getName()); // 닉네임 업데이트
            users.setProfileImage(userInfo.getProfileImage()); // 프로필 사진 업데이트
            userRepository.save(users); // 변경된 정보 저장

            String jwtToken = jwtService.generateToken(users.getEmail());

            // 세션에 accessToken 저장
            session.setAttribute("kakaoToken", kakaoAccessToken);

            // 응답 데이터 준비
            Map<String, Object> response = new HashMap<>();
            //response.put("message", "로그인 성공");
            System.out.println("로그인 성공");
            response.put("jwtToken", jwtToken);
            response.put("login",true);
            return ResponseEntity.ok(response);
        }


        //새로운 유저 가입
        // User 객체 생성
        Users users = new Users(userInfo.getName(), userInfo.getEmail(), userInfo.getProfileImage());
        // 데이터베이스에 사용자 정보 저장
        userRepository.save(users);

        // 세션에 accessToken 저장
        session.setAttribute("kakaoToken", kakaoAccessToken);

        // JWT 토큰 생성
        String jwtToken = jwtService.generateToken(userInfo.getEmail());

        // 응답 데이터 준비
        Map<String, Object> response = new HashMap<>();
        System.out.println("회원가입 성공");
        response.put("jwtToken", jwtToken);
        response.put("login",false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/onboarding")
    public ResponseEntity<String> onboarding(@RequestBody OnboardingDTO onboardingDTO, @RequestHeader(value = "Authorization", required = true) String token) {

        logger.info("Received Authorization Header: {}", token);//로그 확인
        logger.info("Request Body: {}", onboardingDTO);

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                String email = jwtService.extractEmail(jwtToken);
                onboardingService.setUserInfo(email, onboardingDTO.getWakeupTime(), onboardingDTO.getBedTime(), onboardingDTO.getMorningTime(), onboardingDTO.getLunchTime(), onboardingDTO.getDinnerTime(), onboardingDTO.getAlarmMarketing(), onboardingDTO.getAlarmInfo());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT");
            }
        }
        // 로직 처리 후 응답 반환
        return ResponseEntity.ok("Onboarding success");
    }

    //밑은 아직 ing
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
                kakaoService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e) {
                return String.valueOf(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed"));
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
            session.invalidate(); // 세션 무효화
        }else{
            System.out.println("accessToken is null");
        }
        return "redirect:/";
    }

    // 카카오 회원탈퇴
    @PostMapping("/signout")
    public ResponseEntity<String> unlink(@RequestHeader("Authorization") String jwtToken, @RequestBody KakaoSignUpDTO kakaoSignUpDto) {
        String kakaoToken = kakaoSignUpDto.getKakaoAccessToken();
        String email = jwtService.extractEmail(jwtToken.substring(7)); // Bearer 제거 후 파싱
        kakaoService.kakaoUnlink(kakaoToken); //카카오에서 연결 해제
        kakaoService.deleteUser(email); //우리 db에서 회원정보 삭제
        return ResponseEntity.ok("회원정보 삭제 완료");
    }

    // Exception Handler
    @ExceptionHandler(OnboardingService.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(OnboardingService.UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
