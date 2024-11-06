package com.example.Pill_Mate_Backend.domain.oauth2.service;


import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.oauth2.dto.UserInfoResponseDto;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoService {
    private final UserRepository userRepository;

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

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> userInfo = response.getBody();
            String email = (String) ((Map) userInfo.get("kakao_account")).get("email");
            String nickname = (String) ((Map) userInfo.get("properties")).get("nickname");
            String profileImage = (String) ((Map<String, Object>) userInfo.get("properties")).get("profile_image");

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


}