package com.example.Pill_Mate_Backend.domain.check.service;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineCheckDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MedicineCheckService {
    @Autowired
    private MedicineScheduleRepository2 repository;

    public void updateCheckStatus(List<MedicineCheckDTO> updateList) {
        for (MedicineCheckDTO dto : updateList) {
            MedicineSchedule schedule = repository.findById(dto.getMedicineScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("MedicineSchedule not found"));
            schedule.setEatCheck(dto.getEatCheck());
            repository.save(schedule);
        }
    }

    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    /* //여러개..중에 true 하나만 있어도 true(알람 시간 다양하게 넘겨올때)
    public boolean findScheduleIsAfter(List<MedicineCheckDTO> updateList) {
        for (MedicineCheckDTO dto : updateList) {
            MedicineSchedule schedule = repository.findById(dto.getMedicineScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("MedicineSchedule not found"));

            LocalDateTime targetDateTime = LocalDateTime.of(schedule.getIntakeDate(), schedule.getIntakeTime());
            if (targetDateTime.isAfter(LocalDateTime.now())) {
                return true; // 하나라도 미래 시간이면 true 반환
            }
        }
        return false; // 전부 과거 시간이면 false
    }*/

    public boolean findScheduleIsAfter(Long mcId) {
        MedicineSchedule schedule = repository.findById(mcId)
                    .orElseThrow(() -> new ResourceNotFoundException("MedicineSchedule not found"));

        LocalDateTime targetDateTime = LocalDateTime.of(schedule.getIntakeDate(), schedule.getIntakeTime());
        return targetDateTime.isAfter(LocalDateTime.now());
    }
}
