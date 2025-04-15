package com.example.Pill_Mate_Backend.domain.oauth2.service;


import com.example.Pill_Mate_Backend.CommonEntity.RefreshToken;
import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.JwtTokenDto;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.UserInfoResponseDto;
import com.example.Pill_Mate_Backend.domain.oauth2.repository.RefreshTokenRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserInfoResponseDto getUserInfo(String kakaoAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // 카카오 API에 요청할 Authorization 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, request, Map.class
        );
        URI profileImage = URI.create("");

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> userInfo = response.getBody();
            String email = (String) ((Map) userInfo.get("kakao_account")).get("email");
            String nickname = (String) ((Map) userInfo.get("properties")).get("nickname");
            if((((Map<?, ?>) userInfo.get("properties")).get("profile_image") != null)) {
                profileImage = URI.create(((Map<String, Object>) userInfo.get("properties")).get("profile_image").toString());
            }
            return new UserInfoResponseDto(nickname, profileImage, email);
        } else {
            throw new RuntimeException("Failed to get user info from Kakao");
        }
    }

    public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded");

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v1/user/logout",
                    HttpMethod.POST,
                    kakaoLogoutRequest,
                    String.class
            );

            // 응답 확인
            if (response.getStatusCode() == HttpStatus.OK) {
                // 응답이 성공이면 ID를 추출하고 로그아웃 성공 메시지 출력
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                Long id = jsonNode.get("id").asLong();
                System.out.println("카카오 로그아웃 성공, 반환된 id: "+ id);
            } else {
                // 응답이 실패 상태인 경우 로그에 실패 메시지 출력
                System.out.println("카카오 로그아웃 실패, 응답 코드: "+ response.getStatusCode());
                throw new RuntimeException("Failed to log out from Kakao");
            }
        } catch (Exception e) {
            // 예외 발생 시 로그에 오류 메시지 출력
            System.out.println("카카오 로그아웃 중 오류 발생: "+ e.getMessage());
            throw new RuntimeException("카카오 로그아웃 실패", e);
        }
    }

    public void kakaoUnlink(String accessToken) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(unlinkUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 회원탈퇴 성공
            System.out.println("카카오 회원탈퇴 성공");
        } else {
            // 회원탈퇴 실패
            System.out.println("카카오 회원탈퇴 실패");
        }
    }

    public void deleteUser(String email) {
        Optional<Users> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            System.out.println("유저 삭제 완료");
        } else {
            throw new OnboardingService.UserNotFoundException("유저 안보임: " + email);
        }
    }

    public void updateRefreshToken(String email, String refreshtoken){
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email);
        refreshToken.setToken(refreshtoken);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public JwtTokenDto reissue(JwtTokenDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!jwtService.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new GeneralException(ErrorStatus._EXPIRED_REFRESH_JWT_TOKEN);
            //throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        //Authentication authentication = jwtService.getAuthentication(tokenRequestDto.getAccessToken());
        String email = jwtService.extractEmail(tokenRequestDto.getRefreshToken());

        //refreshtoken없을때
        if(refreshTokenRepository.findByEmail(email)==null){
            System.out.println("db에 refresh token 없음");
            throw new GeneralException(ErrorStatus._EXPIRED_REFRESH_JWT_TOKEN);
        }

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        //String refreshToken = tokenRequestDto.getRefreshToken();
        //RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                //.orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email);
        //String refreshToken = refreshTokenRepository.findRefreshTokenByEmail(email);


        System.out.println("DB리프레쉬토큰"+refreshToken.getToken());

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        JwtTokenDto tokenDto = new JwtTokenDto();
        String newAccessToken = "";
        String newRefreshToken = "";
        if (jwtService.refreshTokenPeriodCheck(refreshToken.getToken())) {
            // 5-1. Refresh Token의 유효기간이 3일 미만일 경우 전체(Access / Refresh) 재발급
            newAccessToken = jwtService.generateToken(email);
            newRefreshToken = jwtService.generateRefreshToken(email);
            this.updateRefreshToken(email,newRefreshToken);

            System.out.println("accesstoken, refreshtoken 둘다 발급");

            // 6. Refresh Token 저장소 정보 업데이트
            refreshToken.updateValue(newRefreshToken);
            refreshTokenRepository.save(refreshToken);
        } else {
            // 5-2. Refresh Token의 유효기간이 3일 이상일 경우 Access Token만 재발급
            newAccessToken = jwtService.generateToken(email);
            newRefreshToken = refreshToken.getToken();
            System.out.println("accesstoken 하나만 발급..");
        }
        tokenDto.setAccessToken(newAccessToken);
        tokenDto.setRefreshToken(newRefreshToken);
        System.out.println("tokenDTO: "+tokenDto);
        // 토큰 발급
        return tokenDto;
    }

    //https://gong-story.tistory.com/44
    // https://g-db.tistory.com/entry/Spring-Security-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8-Access-Token%EC%97%90%EC%84%9C-Refresh-Token%EC%B6%94%EA%B0%80%ED%95%98%EC%97%AC-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
    // 참고
}