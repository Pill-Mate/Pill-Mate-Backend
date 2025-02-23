package com.example.Pill_Mate_Backend.domain.management.dto;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ManagementDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CurrentPillResponseDto {
        //복용중인 약물 갯수
        private Integer pillCount;
        private List<CurrentPillResponse> currentPillResponseList;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    //현재 복용중인 약물
    public static class CurrentPillResponse {
        //복용시작 날짜
        private LocalDate startDate;
        // 복용 종료날짜
        private LocalDate endDate;
        // 약물분류명
        private String classname;
        // 약물이름
        private String medicineName;
        // 약물 회사명
        private String entpName;
        //약물사진
        private URI image;

        private Long scheduleId;

        public static CurrentPillResponse from (Schedule schedule) {
            return CurrentPillResponse.builder()
                    .startDate(schedule.getStartDate())
                    //시작일에서 복용일 더하기
                    .endDate(schedule.getStartDate().plusDays(schedule.getIntakePeriod()))
                    .classname(schedule.getMedicine().getClassName())
                    .medicineName(schedule.getMedicine().getMedicineName())
                    .entpName(schedule.getMedicine().getEntpName())
                    .image(schedule.getMedicine().getMedicineImage())
                    .scheduleId(schedule.getId())
                    .build();


        }


    }
}
