package com.example.Pill_Mate_Backend.domain.oauth2.controller;

import com.example.Pill_Mate_Backend.CommonEntity.RefreshToken;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.alarm.service.FcmService;
import com.example.Pill_Mate_Backend.domain.check.dto.ResponseDTO;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.*;
import com.example.Pill_Mate_Backend.domain.oauth2.repository.FcmTokenRepository2;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.KakaoSignUpDTO;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.OnboardingDTO;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.UserInfoResponseDto;
import com.example.Pill_Mate_Backend.domain.oauth2.repository.RefreshTokenRepository;
import com.example.Pill_Mate_Backend.domain.oauth2.service.AppleAuthService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.KakaoService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.OnboardingService;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.lang.reflect.Member;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final KakaoService kakaoService;
    private final OnboardingService onboardingService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    //private final FcmService fcmService;
    //private final FcmTokenRepository2 fcmTokenRepository2;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    //로그 확인
    private final AppleAuthService appleAuthService;

    // 프론트에서 인가코드를 받으면 이 엔드포인트가 호출됨
    @Operation(summary="카카오 회원가입/로그인", description = "카카오 로그인 및 회원가입 모두 처리")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpDTO>> kakaoLogin(
                                                          @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                          @RequestBody KakaoSignUpDTO kakaoSignUpDto,
                                                          HttpSession session) throws IOException {

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

            //onboarding null 시
            if(users.getMorningTime()==null){
                // 기존 유저 정보 업데이트
                users.setUsername(userInfo.getName()); // 닉네임 업데이트
                users.setProfileImage(userInfo.getProfileImage()); // 프로필 사진 업데이트
                userRepository.save(users); // 변경된 정보 저장

                String jwtToken = jwtService.generateToken(users.getEmail());
                String refreshToken = jwtService.generateRefreshToken(users.getEmail());

                // 세션에 accessToken 저장
                session.setAttribute("kakaoToken", kakaoAccessToken);

                //db refresh token 바꾸기...
                kakaoService.updateRefreshToken(userInfo.getEmail(),refreshToken);

                // 응답 데이터 준비
                //Map<String, Object> response = new HashMap<>();
                //response.put("message", "로그인 성공");
                System.out.println("로그인 성공, 온보딩 null");
                //response.put("jwtToken", jwtToken);
                //response.put("refreshToken", refreshToken);
                //response.put("login",false);
                return ResponseEntity.ok(ApiResponse.onSuccess(SignUpDTO.builder()
                        .login(false)
                        .refreshToken(refreshToken)
                        .jwtToken(jwtToken).build()));
            }

            //fcmToken이 새거 일 시(새 디바이스로 로그인 했을 시)
            //List<String> fcmTokens;
            //fcmTokens = fcmTokenRepository2.findFcmTokenByEmail((userInfo.getEmail()));
            //토큰이 null이거나 같은 token이 내부에 없을 시
            //if(fcmTokens==null || !fcmTokenRepository2.existsByUsersAndFcmToken(users, kakaoSignUpDto.getFcmToken())){
            //    System.out.println("fcm토큰 새로 등록");
            //    fcmService.registerToken(users, kakaoSignUpDto.getFcmToken());
            //}

            // 기존 유저 정보 업데이트
            users.setUsername(userInfo.getName()); // 닉네임 업데이트
            users.setProfileImage(userInfo.getProfileImage()); // 프로필 사진 업데이트
            userRepository.save(users); // 변경된 정보 저장

            String jwtToken = jwtService.generateToken(users.getEmail());
            String refreshToken = jwtService.generateRefreshToken(users.getEmail());
            log.info("token: "+ jwtToken);

            // 세션에 accessToken 저장
            session.setAttribute("kakaoToken", kakaoAccessToken);

            //db refresh token 바꾸기...
            kakaoService.updateRefreshToken(userInfo.getEmail(),refreshToken);

            // 응답 데이터 준비
            //Map<String, Object> response = new HashMap<>();
            //response.put("message", "로그인 성공");
            System.out.println("로그인 성공");
            //response.put("jwtToken", jwtToken);
            //response.put("refreshToken", refreshToken);
            //response.put("login",true);
            return ResponseEntity.ok(ApiResponse.onSuccess(SignUpDTO.builder()
                    .login(true)
                    .refreshToken(refreshToken)
                    .jwtToken(jwtToken).build()));
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
        String refreshToken = jwtService.generateRefreshToken(users.getEmail());

        //refreshtoken DB에 저장
        RefreshToken refreshToken1 = new RefreshToken(refreshToken, users);
        refreshTokenRepository.save(refreshToken1);

        //fcmToken;
        //System.out.println(kakaoSignUpDto.getFcmToken());
        //fcmService.registerToken(users, kakaoSignUpDto.getFcmToken());//-----------일단 회원가입할때만 fcmtoken 생성. 계정 당 한개만 있다 상정. 추후 수정.

        // 응답 데이터 준비
        //Map<String, Object> response = new HashMap<>();
        System.out.println("회원가입 성공");
        //response.put("jwtToken", jwtToken);
        //response.put("refreshToken", refreshToken);
        //response.put("login",false);

        return ResponseEntity.ok(ApiResponse.onSuccess(SignUpDTO.builder()
                .login(false)
                .refreshToken(refreshToken)
                .jwtToken(jwtToken).build()));
    }

    @Operation(summary="온보딩", description = "온보딩 정보 받아 삽입")
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<String>> onboarding(@RequestBody OnboardingDTO onboardingDTO, @RequestHeader(value = "Authorization", required = true) String token) {

        logger.info("Received Authorization Header: {}", token);//로그 확인
        logger.info("Request Body: {}", onboardingDTO);

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                String email = jwtService.extractEmail(jwtToken);
                onboardingService.setUserInfo(email, onboardingDTO.getWakeupTime(), onboardingDTO.getBedTime(), onboardingDTO.getMorningTime(), onboardingDTO.getLunchTime(), onboardingDTO.getDinnerTime(), onboardingDTO.getAlarmMarketing(), onboardingDTO.getAlarmInfo());
            } else {
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }
        // 로직 처리 후 응답 반환
        return ResponseEntity.ok(ApiResponse.onSuccess("Onboarding success"));
    }

    @Operation(summary="로그아웃", description = "안쓰는중인 로그아웃,, 아마도")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
                kakaoService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.onFailure("Logout failed"));
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
            session.invalidate(); // 세션 무효화
        }else{
            System.out.println("accessToken is null");
        }
        return ResponseEntity.ok(ApiResponse.onSuccess("redirect:/"));
    }

    // 카카오 회원탈퇴
    @Operation(summary="카카오, 애플 회원탈퇴", description = "카카오 회원탈퇴:카카오와 연결 끊기 / 애플 회원탈퇴:kakaoaccesstoken null 시, 애플과 연결 해제")
    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<String>> unlink(@RequestHeader("Authorization") String jwtToken, @RequestBody KakaoSignOutDTO kakaoSignUpDto) {
        String kakaoToken = kakaoSignUpDto.getKakaoAccessToken();
        String email = jwtService.extractEmail(jwtToken.substring(7)); // Bearer 제거 후 파싱

        //apple Sign out
        if(kakaoToken == null || "".equals(kakaoToken)){
            //애플이랑 연결해제(revoke)
            appleAuthService.revoke(email);
            System.out.println(email+": 애플이랑 연동 해제");
            kakaoService.deleteUser(email); //우리 db에서 회원정보 삭제
            return ResponseEntity.ok(ApiResponse.onSuccess("애플 회원정보 삭제 완료"));
        }

        kakaoService.kakaoUnlink(kakaoToken); //카카오에서 연결 해제
        kakaoService.deleteUser(email); //우리 db에서 회원정보 삭제
        return ResponseEntity.ok(ApiResponse.onSuccess("회원정보 삭제 완료"));
    }

    @Operation(summary="jwt토큰 검증", description = "jwt 토큰 만료 시 자동 로그인 위한 토큰 검증 - refresh 토큰 있을 시 jwt 발급 후 자동 로그인, 없을 시 재 로그인")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<JwtTokenDto>> reissue(@RequestBody JwtTokenDto tokenRequestDto) {
        return ResponseEntity.ok(ApiResponse.onSuccess(kakaoService.reissue(tokenRequestDto)));
    }

    // Exception Handler
    @ExceptionHandler(OnboardingService.UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(OnboardingService.UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ex.getMessage()));
    }

    @Operation(summary="애플 회원가입/로그인", description = "애플,, 로그인..")
    @PostMapping("/appleSignup")
    public ResponseEntity<ApiResponse<SignUpDTO>> appleLogin(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody AppleSignUpDTO appleSignUpDTO,
            HttpSession session) throws IOException, AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException,
    JsonProcessingException{

        // JWT가 존재하는 경우에만 처리
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            // JWT를 사용한 추가 처리 가능
        }

        String accountId = appleAuthService.getAppleAccountId(appleSignUpDTO.getIdentityToken());

        // appleId로 유저가 이미 존재하는지 확인
        Optional<Users> existingUser = userRepository.findByAppleId(accountId);
        if (existingUser.isPresent()) {
            // 이미 존재하는 유저 -> 로그인 처리
            Users users = existingUser.get();

            //onboarding null 시
            if(users.getMorningTime()==null){

                String jwtToken = jwtService.generateToken(users.getEmail());
                String refreshToken = jwtService.generateRefreshToken(users.getEmail());

                //db refresh token 바꾸기...
                kakaoService.updateRefreshToken(users.getEmail(),refreshToken);

                System.out.println("로그인 성공, 온보딩 null");

                return ResponseEntity.ok(ApiResponse.onSuccess(SignUpDTO.builder()
                        .login(false)
                        .refreshToken(refreshToken)
                        .jwtToken(jwtToken).build()));
            }

            String jwtToken = jwtService.generateToken(users.getEmail());
            String refreshToken = jwtService.generateRefreshToken(users.getEmail());
            log.info("token: "+ jwtToken);

            //db refresh token 바꾸기...
            kakaoService.updateRefreshToken(users.getEmail(),refreshToken);

            // 응답 데이터 준비
            System.out.println("로그인 성공");
            return ResponseEntity.ok(ApiResponse.onSuccess(SignUpDTO.builder()
                    .login(true)
                    .refreshToken(refreshToken)
                    .jwtToken(jwtToken).build()));
        }else{
            //새로운 유저 가입
            System.out.println("email=[" + appleSignUpDTO.getEmail() + "]");
            System.out.println("userName=[" + appleSignUpDTO.getUserName() + "]");
            System.out.println("identityToken=[" + appleSignUpDTO.getIdentityToken() + "]");


            //요소없이 생성 금지
            if (
                    appleSignUpDTO.getEmail() == null || appleSignUpDTO.getEmail().isBlank() ||
                            appleSignUpDTO.getUserName() == null || appleSignUpDTO.getUserName().isBlank() ||
                            appleSignUpDTO.getIdentityToken() == null || appleSignUpDTO.getIdentityToken().isBlank()
            ) {
                System.out.println("USERS 생성 위한 요소 불충분");
                throw new GeneralException(ErrorStatus._USERS_ELEMENT_LACK);
            }

            //apple refreshtoken 받기.
            String appleRefreshToken = appleAuthService.exchange(appleSignUpDTO.getAuthorizationCode());

            // User 객체 생성(apple Id, apple refreshToken 저장)
            Users users = new Users(appleSignUpDTO.getUserName(), appleSignUpDTO.getEmail(), accountId, appleRefreshToken);
            // 데이터베이스에 사용자 정보 저장
            userRepository.save(users);

            // JWT 토큰 생성
            String jwtToken = jwtService.generateToken(appleSignUpDTO.getEmail());
            String refreshToken = jwtService.generateRefreshToken(appleSignUpDTO.getEmail());

            //refreshtoken DB에 저장
            RefreshToken refreshToken1 = new RefreshToken(refreshToken, users);
            refreshTokenRepository.save(refreshToken1);

            System.out.println("회원가입 성공");

            return ResponseEntity.ok(ApiResponse.onSuccess(SignUpDTO.builder()
                    .login(false)
                    .refreshToken(refreshToken)
                    .jwtToken(jwtToken).build()));
        }
    }
}
