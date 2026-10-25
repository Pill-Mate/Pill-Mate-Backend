package com.example.Pill_Mate_Backend.domain.statistics.dto;

import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
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

            @Schema(description = "복용 시작 이후 누적 복용일수(한 번이라도 체크한 날 수)", example = "14")
            long totalIntakeDays
    ) {
        public static MedicineOption of(Schedule schedule, long totalIntakeDays) {
            return MedicineOption.builder()
                    .medicineId(schedule.getMedicine().getId())
                    .scheduleId(schedule.getId())
                    .medicineName(schedule.getMedicine().getMedicineName())
                    .totalIntakeDays(totalIntakeDays)
                    .build();
        }
    }

    public enum DayStatus {
        TAKEN,      // 한 번이라도 체크(파란색)
        NOT_TAKEN   // 체크 없음(회색)
    }

    @Builder
    public record CalendarDay(
            @Schema(description = "날짜", example = "2024-11-01")
            LocalDate date,

            @Schema(description = "복용 상태(TAKEN: 한 번이라도 체크, NOT_TAKEN: 체크 없음)", example = "TAKEN")
            DayStatus status
    ) {}

    @Builder
    public record CalendarResponse(
            @Schema(description = "약물 이름", example = "오그맥스정")
            String medicineName,

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
