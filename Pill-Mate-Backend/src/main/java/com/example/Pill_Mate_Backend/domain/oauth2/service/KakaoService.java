package com.example.Pill_Mate_Backend.domain.oauth2.service;


import com.example.Pill_Mate_Backend.CommonEntity.User;
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

    public UserInfoResponseDto getUserInfo(String kakaoAccessToken, boolean alarmMarketing) {
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

            //? 유저...데이터 넣기
            //User user = new User(nickname, email, alarmMarketing, profileImage);
            //userRepository.save(user);

            return new UserInfoResponseDto(nickname, profileImage, email, alarmMarketing);
        } else {
            throw new RuntimeException("Failed to get user info from Kakao");
        }
        /*
        Map<String, Object> userInfo = getKakaoUserInfo(kakaoAccessToken);

        String userName = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");
        String profileImage = (String) ((Map<String, Object>) userInfo.get("properties")).get("profile_image");
        String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(userName);
            existingUser.setProfileImage(profileImage);
            existingUser.setEmail(email);
            userRepository.save(existingUser);
        } else {
            // 회원가입 처리 시 alarmMarketing과 같은 추가 정보도 설정
            User newUser = new User(profileImage, email, alarmMarketing);
            userRepository.save(newUser);
        }

        return new UserInfoResponseDto(userName, profileImage, email, alarmMarketing);*/
    }

    private Map<String, Object> getKakaoUserInfo(String kakaoAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBody = response.getBody();
            if (responseBody instanceof Map) {
                return (Map<String, Object>) responseBody;
            } else {
                throw new RuntimeException("Unexpected response body type");
            }
        }
        throw new RuntimeException("Failed to get user info");
    }


    //삭제?0---------------------------------------------------------------------------------------------------------------------------------
    public void kakaoLogout(String accessToken) {
        String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 로그아웃 성공
            System.out.println("카카오 로그아웃 성공");
        } else {
            // 로그아웃 실패
            System.out.println("카카오 로그아웃 실패");
        }
    }

    public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
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

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        System.out.println("반환된 id: "+id);
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

    public void userLogout(HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키 삭제 등 추가 작업
    }

    public void deleteUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            System.out.println("유저 삭제 완료");
        } else {
            throw new OnboardingService.UserNotFoundException("유저 안보임: " + email);
        }
    }


}