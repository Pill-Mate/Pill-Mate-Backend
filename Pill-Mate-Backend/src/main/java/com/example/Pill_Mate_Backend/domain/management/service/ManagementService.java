package com.example.Pill_Mate_Backend.domain.management.service;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.management.dto.ManagementDto;
import com.example.Pill_Mate_Backend.domain.register.repository.ScheduleRepository;
import com.example.Pill_Mate_Backend.domain.register.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ManagementService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public ManagementDto.CurrentPillResponseDto getCurrentList(String email) {
            List<Schedule> schedules = scheduleRepository.findByUsersIdAndStatus(userRepository.findIdxByEmail(email).orElseThrow(), ScheduleStatus.ACTIVATE);
            List<ManagementDto.CurrentPillResponse>dto = schedules.stream().map(ManagementDto.CurrentPillResponse::from).collect(Collectors.toList());
            return ManagementDto.CurrentPillResponseDto.builder().pillCount(dto.size()).currentPillResponseList(dto).build();
    }

    public List<ManagementDto.CurrentPillResponse> getStopList(String email) {
        List<Schedule> schedules = scheduleRepository.findByUsersIdAndStatus(userRepository.findIdxByEmail(email).orElseThrow(), ScheduleStatus.INACTIVATE);
        return schedules.stream().map(ManagementDto.CurrentPillResponse::from).collect(Collectors.toList());
    }
}
