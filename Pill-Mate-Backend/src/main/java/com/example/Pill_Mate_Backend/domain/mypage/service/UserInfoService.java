package com.example.Pill_Mate_Backend.domain.mypage.service;

import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDetailDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository2;
import com.example.Pill_Mate_Backend.domain.mypage.dto.UserInfoDTO;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;


@RequiredArgsConstructor
@Service
public class UserInfoService {
    @Autowired
    private UsersRepository usersRepository;

    public UserInfoDTO getUserInfoByEmail(String email) {
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
    }
}
