package com.example.Pill_Mate_Backend.domain.check.service;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineCheckDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
