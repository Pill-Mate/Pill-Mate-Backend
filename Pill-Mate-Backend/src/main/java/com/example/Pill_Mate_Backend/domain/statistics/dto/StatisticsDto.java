package com.example.Pill_Mate_Backend.domain.statistics.dto;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class StatisticsDto {

    @Builder
    public record MedicineOption(
            @Schema(description = "약물 ID", example = "1")
            Long medicineId,

            @Schema(description = "스케줄 ID", example = "1")
            Long scheduleId,

            @Schema(description = "약물 이름", example = "오그맥스정")
            String medicineName,

            @Schema(description = "복용 상태(ACTIVATE: 복용중, INACTIVATE: 복용중지)", example = "ACTIVATE")
            ScheduleStatus status
    ) {
        public static MedicineOption from(Schedule schedule) {
            return MedicineOption.builder()
                    .medicineId(schedule.getMedicine().getId())
                    .scheduleId(schedule.getId())
                    .medicineName(schedule.getMedicine().getMedicineName())
                    .status(schedule.getStatus())
                    .build();
        }
    }

    public enum DayStatus {
        TAKEN,      // 해당 날짜 복용분 모두 체크
        PARTIAL,    // 일부만 체크
        MISSED,     // 지난 날짜인데 하나도 체크 안 함
        SCHEDULED   // 오늘/미래 날짜, 아직 체크 안 함
    }

    @Builder
    public record CalendarDay(
            @Schema(description = "날짜", example = "2024-11-01")
            LocalDate date,

            @Schema(description = "복용 상태(TAKEN, PARTIAL, MISSED, SCHEDULED)", example = "TAKEN")
            DayStatus status
    ) {}

    @Builder
    public record CalendarResponse(
            @Schema(description = "약물 이름", example = "오그맥스정")
            String medicineName,

            @Schema(description = "해당 월 복용 완료(TAKEN) 일수", example = "14")
            int intakeDayCount,

            @Schema(description = "복용 일정이 있는 날짜별 상태(일정 없는 날짜는 포함되지 않음)")
            List<CalendarDay> days
    ) {}

    @Builder
    public record MonthlyCount(
            @Schema(description = "연월", example = "2024-11")
            String yearMonth,

            @Schema(description = "복용 약물 종류 수", example = "7")
            long count
    ) {}

    @Builder
    public record CountTrendResponse(
            @Schema(description = "지난달 대비 증감(음수면 감소)", example = "-1")
            long diffFromLastMonth,

            @Schema(description = "권장 복약 개수(이하)", example = "7")
            int recommendedMax,

            @Schema(description = "월별 약물 종류 수(오래된 달부터)")
            List<MonthlyCount> trend
    ) {}
}
