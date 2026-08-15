package com.example.Pill_Mate_Backend.domain.check.service;

import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import com.example.Pill_Mate_Backend.domain.check.dto.WeekCountDTO;
import com.example.Pill_Mate_Backend.domain.check.dto.WeekDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RequiredArgsConstructor
@Service
public class HomeService {

    @Autowired
    private MedicineScheduleRepository2 medicineScheduleRepository2;


    @SneakyThrows
    public List<MedicineDTO> getMedicineSchedulesByDate(String email, LocalDate date) {
        List<Object[]> results = medicineScheduleRepository2.findByIntakeDate(email, date);
        List<MedicineDTO> medicineDTOList = new ArrayList<>();

        for (Object[] result : results) {

            Long itemSeq = result[9] != null ? Long.parseLong(result[9].toString()) : null;

            String medicineImageUrl = null;
            Object rawImage = result[10];

            if (rawImage != null) {
                if (rawImage instanceof String s) {
                    medicineImageUrl = s.isBlank() ? null : s;
                } else {
                    // 혹시 드라이버/쿼리 설정에 따라 다른 타입으로 오면 문자열로 변환
                    String s = rawImage.toString();
                    medicineImageUrl = (s == null || s.isBlank()) ? null : s;
                }
            }

            MedicineDTO dto = new MedicineDTO(
                    (Long) result[0],
                    (String) result[1],
                    (Time) result[2],
                    (Integer) result[3],
                    (String) result[4],
                    (Integer) result[5],
                    (String) result[6],
                    (Boolean) result[7],
                    (String) result[8],
                    itemSeq,
                    medicineImageUrl
            );

            medicineDTOList.add(dto);
        }

        return medicineDTOList;
    }



    public WeekCountDTO getWeekCountByDate(String email, LocalDate date){
        Object[] countAllResult = medicineScheduleRepository2.findAllCountByDate(email, date);
        Object[] countLeftResult = medicineScheduleRepository2.findLeftCountByDate(email, date);

        //print-후 삭제
        System.out.println("countall:"+ countAllResult[0]);
        System.out.println("countleft:"+countLeftResult[0]);

        //date 범위 알아내기(일주일 범위 알아내기)
        Map<String, LocalDate> weekRange = getWeekRange(date);
        LocalDate startDate = weekRange.get("startOfWeek");
        LocalDate endDate = weekRange.get("endOfWeek");
        System.out.println("Date: " + startDate + " - " + endDate);

        Boolean sunday = false;
        Boolean monday = false;
        Boolean tuesday = false;
        Boolean wednesday = false;
        Boolean thursday = false;
        Boolean friday = false;
        Boolean saturday = false;

        List<Object[]> weekResult = medicineScheduleRepository2.findExitWeekByDate(email, startDate, endDate);
        for (Object[] result : weekResult){
            DayOfWeek currentDayOfWeek = toLocalDate(result[0]).getDayOfWeek();

            if(currentDayOfWeek == DayOfWeek.SUNDAY ) {sunday = true;}
            else if(currentDayOfWeek == DayOfWeek.MONDAY ) {monday = true;}
            else if(currentDayOfWeek == DayOfWeek.TUESDAY ) {tuesday = true;}
            else if(currentDayOfWeek == DayOfWeek.WEDNESDAY ) {wednesday = true;}
            else if(currentDayOfWeek == DayOfWeek.THURSDAY ) {thursday = true;}
            else if(currentDayOfWeek == DayOfWeek.FRIDAY ) {friday = true;}
            else if(currentDayOfWeek == DayOfWeek.SATURDAY ) {saturday = true;}
        }

        System.out.println("week:"+sunday+monday+tuesday+wednesday+thursday+friday+saturday);

        WeekCountDTO weekCountDTO = new WeekCountDTO(
                sunday, monday, tuesday, wednesday, thursday, friday, saturday,
                Long.valueOf(String.valueOf(Optional.ofNullable(countAllResult[0]).orElse(0L))).intValue(),
                Long.valueOf(String.valueOf(Optional.ofNullable(countLeftResult[0]).orElse(0L))).intValue()
        );
        return weekCountDTO;
    }

    public WeekDTO getWeekByDate(String email, LocalDate date){

        //date 범위 알아내기(일주일 범위 알아내기)
        Map<String, LocalDate> weekRange = getWeekRange(date);
        LocalDate startDate = weekRange.get("startOfWeek");
        LocalDate endDate = weekRange.get("endOfWeek");
        System.out.println("Date: " + startDate + " - " + endDate);

        Boolean sunday = false;
        Boolean monday = false;
        Boolean tuesday = false;
        Boolean wednesday = false;
        Boolean thursday = false;
        Boolean friday = false;
        Boolean saturday = false;

        List<Object[]> weekResult = medicineScheduleRepository2.findExitWeekByDate(email, startDate, endDate);
        for (Object[] result : weekResult){
            DayOfWeek currentDayOfWeek = toLocalDate(result[0]).getDayOfWeek();

            if(currentDayOfWeek == DayOfWeek.SUNDAY ) {sunday = true;}
            else if(currentDayOfWeek == DayOfWeek.MONDAY ) {monday = true;}
            else if(currentDayOfWeek == DayOfWeek.TUESDAY ) {tuesday = true;}
            else if(currentDayOfWeek == DayOfWeek.WEDNESDAY ) {wednesday = true;}
            else if(currentDayOfWeek == DayOfWeek.THURSDAY ) {thursday = true;}
            else if(currentDayOfWeek == DayOfWeek.FRIDAY ) {friday = true;}
            else if(currentDayOfWeek == DayOfWeek.SATURDAY ) {saturday = true;}
        }

        System.out.println("week:"+sunday+monday+tuesday+wednesday+thursday+friday+saturday);

        WeekDTO weekDTO = new WeekDTO(
                sunday, monday, tuesday, wednesday, thursday, friday, saturday
        );
        return weekDTO;
    }

    public static Map<String, LocalDate> getWeekRange(LocalDate date) {
        // 해당 주의 시작 날짜 (일요일)
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // 해당 주의 종료 날짜 (토요일)
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // 결과를 Map으로 반환
        Map<String, LocalDate> weekRange = new HashMap<>();
        weekRange.put("startOfWeek", startOfWeek);
        weekRange.put("endOfWeek", endOfWeek);

        return weekRange;
    }

    //
    public LocalDate getDateByScheduleId(Long MedicineScheduleId){
        Object[] obj = medicineScheduleRepository2.findDateByMedicineScheduleId(MedicineScheduleId);
        if (obj == null || obj.length == 0 || obj[0] == null) {
            return null;
        }
        return toLocalDate(obj[0]);
    }

    // 네이티브 쿼리의 DATE 컬럼은 드라이버 설정에 따라 java.sql.Date 또는 LocalDate로 올 수 있음
    private static LocalDate toLocalDate(Object rawDate) {
        if (rawDate instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        if (rawDate instanceof LocalDate localDate) {
            return localDate;
        }
        return LocalDate.parse(rawDate.toString());
    }
}
