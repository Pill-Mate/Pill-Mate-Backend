package com.example.Pill_Mate_Backend.domain.check.service;

//import com.example.Pill_Mate_Backend.CommonEntity.enums.IntakeSpecific;
import com.example.Pill_Mate_Backend.domain.check.dto.MedicineDTO;
import com.example.Pill_Mate_Backend.domain.check.dto.WeekCountDTO;
import com.example.Pill_Mate_Backend.domain.check.dto.WeekDTO;
import com.example.Pill_Mate_Backend.domain.check.repository.MedicineScheduleRepository2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Time;
import java.util.*;

@RequiredArgsConstructor
@Service
public class HomeService {

    @Autowired
    private MedicineScheduleRepository2 medicineScheduleRepository2;


    @SneakyThrows
    public List<MedicineDTO> getMedicineSchedulesByDate(String email, Date date) {
        List<Object[]> results = medicineScheduleRepository2.findByIntakeDate(email, date);
        List<MedicineDTO> medicineDTOList = new ArrayList<>();

        for (Object[] result : results) {
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
                    result[9] instanceof byte[] ? new URI(new String((byte[]) result[9])) : null // 바이너리 데이터를 String으로 변환 후 URI로 파싱
            );
            medicineDTOList.add(dto);
        }

        return medicineDTOList;
    }

    public WeekCountDTO getWeekCountByDate(String email, Date date){
        Object[] countAllResult = medicineScheduleRepository2.findAllCountByDate(email, date);
        Object[] countLeftResult = medicineScheduleRepository2.findLeftCountByDate(email, date);

        //print-후 삭제
        System.out.println("countall:"+ countAllResult[0]);
        System.out.println("countleft:"+countLeftResult[0]);

        //date 범위 알아내기(일주일 범위 알아내기)
        Map<String, Date> weekRange = getWeekRange(date);
        Date startDate = weekRange.get("startOfWeek");
        Date endDate = weekRange.get("endOfWeek");
        System.out.println("Date: " + weekRange.get("startOfWeek") + " - " + weekRange.get("endOfWeek"));

        Boolean sunday = false;
        Boolean monday = false;
        Boolean tuesday = false;
        Boolean wednesday = false;
        Boolean thursday = false;
        Boolean friday = false;
        Boolean saturday = false;

        List<Object[]> weekResult = medicineScheduleRepository2.findExitWeekByDate(email, startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        for (Object[] result : weekResult){
            calendar.setTime((Date) result[0]);

            // 현재 날짜의 요일을 가져옴 (1: Sunday, 2: Monday, ..., 7: Saturday)
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if(currentDayOfWeek ==1 ) {sunday = true;}
            else if(currentDayOfWeek == 2 ) {monday = true;}
            else if(currentDayOfWeek == 3 ) {tuesday = true;}
            else if(currentDayOfWeek == 4 ) {wednesday = true;}
            else if(currentDayOfWeek == 5 ) {thursday = true;}
            else if(currentDayOfWeek == 6 ) {friday = true;}
            else if(currentDayOfWeek == 7 ) {saturday = true;}
        }

        System.out.println("week:"+sunday+monday+tuesday+wednesday+thursday+friday+saturday);

        WeekCountDTO weekCountDTO = new WeekCountDTO(
                sunday, monday, tuesday, wednesday, thursday, friday, saturday,
                Long.valueOf(String.valueOf(Optional.ofNullable(countAllResult[0]).orElse(0L))).intValue(),
                Long.valueOf(String.valueOf(Optional.ofNullable(countLeftResult[0]).orElse(0L))).intValue()
        );
        return weekCountDTO;
    }

    public WeekDTO getWeekByDate(String email, Date date){

        //date 범위 알아내기(일주일 범위 알아내기)
        Map<String, Date> weekRange = getWeekRange(date);
        Date startDate = weekRange.get("startOfWeek");
        Date endDate = weekRange.get("endOfWeek");
        System.out.println("Date: " + weekRange.get("startOfWeek") + " - " + weekRange.get("endOfWeek"));

        Boolean sunday = false;
        Boolean monday = false;
        Boolean tuesday = false;
        Boolean wednesday = false;
        Boolean thursday = false;
        Boolean friday = false;
        Boolean saturday = false;

        List<Object[]> weekResult = medicineScheduleRepository2.findExitWeekByDate(email, startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        for (Object[] result : weekResult){
            calendar.setTime((Date) result[0]);

            // 현재 날짜의 요일을 가져옴 (1: Sunday, 2: Monday, ..., 7: Saturday)
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if(currentDayOfWeek ==1 ) {sunday = true;}
            else if(currentDayOfWeek == 2 ) {monday = true;}
            else if(currentDayOfWeek == 3 ) {tuesday = true;}
            else if(currentDayOfWeek == 4 ) {wednesday = true;}
            else if(currentDayOfWeek == 5 ) {thursday = true;}
            else if(currentDayOfWeek == 6 ) {friday = true;}
            else if(currentDayOfWeek == 7 ) {saturday = true;}
        }

        System.out.println("week:"+sunday+monday+tuesday+wednesday+thursday+friday+saturday);

        WeekDTO weekDTO = new WeekDTO(
                sunday, monday, tuesday, wednesday, thursday, friday, saturday
        );
        return weekDTO;
    }

    public static Map<String, Date> getWeekRange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 현재 날짜의 요일을 가져옴 (1: Sunday, 2: Monday, ..., 7: Saturday)
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 해당 주의 시작 날짜 (일요일)
        calendar.add(Calendar.DAY_OF_MONTH, - (currentDayOfWeek - Calendar.SUNDAY));
        Date startOfWeek = calendar.getTime();

        // 해당 주의 종료 날짜 (토요일)
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        Date endOfWeek = calendar.getTime();

        // 결과를 Map으로 반환
        Map<String, Date> weekRange = new HashMap<>();
        weekRange.put("startOfWeek", startOfWeek);
        weekRange.put("endOfWeek", endOfWeek);

        return weekRange;
    }
}
