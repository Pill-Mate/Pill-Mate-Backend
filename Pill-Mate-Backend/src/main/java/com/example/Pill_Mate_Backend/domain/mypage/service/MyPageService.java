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
}
