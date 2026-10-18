package com.example.Pill_Mate_Backend.domain.statistics.controller;

import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.statistics.dto.StatisticsDto;
import com.example.Pill_Mate_Backend.domain.statistics.service.StatisticsService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@RestController
public class StatisticsController {
    private static final int MAX_TREND_MONTHS = 12;

    private final StatisticsService statisticsService;
    private final JwtService jwtService;

    @Operation(summary = "통계 약물 드롭다운 리스트", description = "복용중/복용중지 약물을 모두 조회합니다. 복용중 약물이 먼저 정렬됩니다.")
    @GetMapping("/medicines")
    public ResponseEntity<ApiResponse<List<StatisticsDto.MedicineOption>>> medicineOptions(
            @RequestHeader(value = "Authorization", required = true) String token) {
        String email = extractEmail(token);
        return ResponseEntity.ok(ApiResponse.onSuccess(statisticsService.getMedicineOptions(email)));
    }

    @Operation(summary = "개별 약물 복용 달력", description = "해당 월의 날짜별 복용 상태와 복용 완료 일수를 조회합니다.")
    @GetMapping("/medicines/{medicineId}/calendar")
    public ResponseEntity<ApiResponse<StatisticsDto.CalendarResponse>> calendar(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable("medicineId") Long medicineId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        String email = extractEmail(token);
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.of(year, month);
        } catch (DateTimeException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(statisticsService.getCalendar(email, medicineId, yearMonth)));
    }

    @Operation(summary = "약물 종류 개수 추이", description = "endMonth(yyyy-MM, 기본 이번 달)까지 최근 months개월(기본 7, 최대 12)의 월별 약물 종류 수를 조회합니다.")
    @GetMapping("/medicine-count-trend")
    public ResponseEntity<ApiResponse<StatisticsDto.CountTrendResponse>> countTrend(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam(value = "endMonth", required = false) String endMonth,
            @RequestParam(value = "months", defaultValue = "7") int months) {
        String email = extractEmail(token);
        if (months < 1 || months > MAX_TREND_MONTHS) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        YearMonth end;
        try {
            end = endMonth == null ? statisticsService.currentMonth() : YearMonth.parse(endMonth);
        } catch (DateTimeParseException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(statisticsService.getCountTrend(email, end, months)));
    }

    private String extractEmail(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                return jwtService.extractEmail(jwtToken);
            }
            log.info("Invalid JWT");
            throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
        }
        throw new GeneralException(ErrorStatus._UNAUTHORIZED);
    }
}
