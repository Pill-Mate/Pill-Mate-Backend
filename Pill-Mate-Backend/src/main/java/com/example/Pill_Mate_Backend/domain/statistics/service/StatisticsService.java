package com.example.Pill_Mate_Backend.domain.statistics.service;

import com.example.Pill_Mate_Backend.CommonEntity.MedicineSchedule;
import com.example.Pill_Mate_Backend.CommonEntity.Schedule;
import com.example.Pill_Mate_Backend.CommonEntity.enums.ScheduleStatus;
import com.example.Pill_Mate_Backend.domain.statistics.dto.StatisticsDto;
import com.example.Pill_Mate_Backend.domain.statistics.repository.StatisticsRepository;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.handler.MedicineHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StatisticsService {
    private static final ZoneId ZONE_SEOUL = ZoneId.of("Asia/Seoul");
    private static final int RECOMMENDED_MAX = 7;

    private final StatisticsRepository statisticsRepository;

    public List<StatisticsDto.MedicineOption> getMedicineOptions(String email) {
        return statisticsRepository.findSchedulesByEmail(email).stream()
                // 복용중 약물 먼저
                .sorted(Comparator.comparing((Schedule s) -> s.getStatus() != ScheduleStatus.ACTIVATE)
                        .thenComparing(Schedule::getId))
                .map(StatisticsDto.MedicineOption::from)
                .collect(Collectors.toList());
    }

    public StatisticsDto.CalendarResponse getCalendar(String email, Long medicineId, YearMonth yearMonth) {
        Schedule schedule = statisticsRepository.findScheduleByMedicineIdAndEmail(medicineId, email)
                .orElseThrow(() -> new MedicineHandler(ErrorStatus._MEDICINE_NOT_FOUND));

        List<MedicineSchedule> intakes = statisticsRepository.findIntakesInPeriod(
                medicineId, email, yearMonth.atDay(1), yearMonth.atEndOfMonth());

        Map<LocalDate, List<MedicineSchedule>> byDate = intakes.stream()
                .collect(Collectors.groupingBy(MedicineSchedule::getIntakeDate, TreeMap::new, Collectors.toList()));

        LocalDate today = LocalDate.now(ZONE_SEOUL);
        List<StatisticsDto.CalendarDay> days = new ArrayList<>();
        byDate.forEach((date, list) -> days.add(StatisticsDto.CalendarDay.builder()
                .date(date)
                .status(toDayStatus(date, list, today))
                .build()));

        int intakeDayCount = (int) days.stream()
                .filter(d -> d.status() == StatisticsDto.DayStatus.TAKEN)
                .count();

        return StatisticsDto.CalendarResponse.builder()
                .medicineName(schedule.getMedicine().getMedicineName())
                .intakeDayCount(intakeDayCount)
                .days(days)
                .build();
    }

    public StatisticsDto.CountTrendResponse getCountTrend(String email, YearMonth endMonth, int months) {
        YearMonth startMonth = endMonth.minusMonths(months - 1);

        Map<YearMonth, Long> countByMonth = new HashMap<>();
        for (Object[] row : statisticsRepository.countMedicineKindsByMonth(
                email, startMonth.atDay(1), endMonth.atEndOfMonth())) {
            YearMonth ym = YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue());
            countByMonth.put(ym, ((Number) row[2]).longValue());
        }

        // 기록 없는 달은 0으로 채움
        List<StatisticsDto.MonthlyCount> trend = new ArrayList<>();
        for (YearMonth ym = startMonth; !ym.isAfter(endMonth); ym = ym.plusMonths(1)) {
            trend.add(StatisticsDto.MonthlyCount.builder()
                    .yearMonth(ym.toString())
                    .count(countByMonth.getOrDefault(ym, 0L))
                    .build());
        }

        long current = countByMonth.getOrDefault(endMonth, 0L);
        long last = countByMonth.getOrDefault(endMonth.minusMonths(1), 0L);

        return StatisticsDto.CountTrendResponse.builder()
                .diffFromLastMonth(current - last)
                .recommendedMax(RECOMMENDED_MAX)
                .trend(trend)
                .build();
    }

    public YearMonth currentMonth() {
        return YearMonth.now(ZONE_SEOUL);
    }

    private StatisticsDto.DayStatus toDayStatus(LocalDate date, List<MedicineSchedule> list, LocalDate today) {
        long checked = list.stream().filter(ms -> Boolean.TRUE.equals(ms.getEatCheck())).count();
        if (checked == list.size()) {
            return StatisticsDto.DayStatus.TAKEN;
        }
        if (checked > 0) {
            return StatisticsDto.DayStatus.PARTIAL;
        }
        return date.isBefore(today) ? StatisticsDto.DayStatus.MISSED : StatisticsDto.DayStatus.SCHEDULED;
    }
}
