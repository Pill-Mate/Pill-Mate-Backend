package com.example.Pill_Mate_Backend.domain.mypage.service;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.mypage.dto.MyPageDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class MyPageService {
    @Autowired
    private UsersRepository usersRepository;

    public MyPageDTO getMyPageByEmail(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        MyPageDTO myPageDTO = new MyPageDTO(
                users.getUsername(),
                email,
                users.getProfileImage().toString(),
                users.getAlarmMarketing(),
                users.getAlarmInfo()
        );
        System.out.println("mypagedto2: "+myPageDTO);

        return myPageDTO;
    }
    /*
    //uri byte 변환 코드 예시
    List<Object[]> results = usersRepository.findUserInfoByEmail(email);

        // 결과가 비어있는 경우 예외 던지기
        if (results.isEmpty()) {
            throw new RuntimeException("No user found with email: " + email);
        }

        Object[] result = results.get(0); // 첫 번째 행의 결과
        System.out.println("Result length: " + result.length);
        System.out.println("Result[0] type: " + result[0].getClass().getName());
        System.out.println("Result[1] type: " + result[1].getClass().getName());


        byte[] byteArray = (byte[]) result[0];

        URI profileImage = null;
        // 직렬화 해제 (Deserialization) - 직렬화된 URI 객체를 복원
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object deserializedObject = objectInputStream.readObject();
            if (deserializedObject instanceof URI) {
                profileImage = (URI) deserializedObject;
            } else {
                throw new IllegalArgumentException("Deserialized object is not a URI");
            }
        } catch (Exception e) {
        throw new RuntimeException("Failed to deserialize profile_image", e);
        }

        String username = (String) result[1];

        UserInfoDTO userInfoDTO = new UserInfoDTO(
                username,
                email,
                profileImage.toString()
        );

        return userInfoDTO;
    */
}
