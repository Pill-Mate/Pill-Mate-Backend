package com.example.Pill_Mate_Backend.domain.conflict.controller;

import com.example.Pill_Mate_Backend.domain.conflict.dto.AllConflictResponse;
import com.example.Pill_Mate_Backend.domain.conflict.dto.PhoneAddresses;
import com.example.Pill_Mate_Backend.domain.conflict.service.ApiService;
import com.example.Pill_Mate_Backend.domain.conflict.service.EfcyApiService;
import com.example.Pill_Mate_Backend.domain.conflict.service.MedicineService;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.register.repository.MedicineScheduleRepository;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;


@Slf4j
@RestController
@RequestMapping("/api/v1/dur")
@RequiredArgsConstructor
public class DurApiController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private EfcyApiService efcyApiService;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private MedicineService medicineService;


    @Value("${openApi.serviceKey}")
    private String serviceKey;
    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;

    @Operation(summary = "전체 약물 충돌 검사", description = "itemSeq 기반으로 DUR 병용금기/효능군 중복, 서버 내 내 약물과의 충돌 여부를 모두 검사")
    @GetMapping("/check-conflict")
    public ApiResponse<AllConflictResponse> checkConflictAll(
            @RequestParam String itemSeq,
            @RequestHeader(value = "Authorization", required = true) String token) {

        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);
            } else {
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        return ApiResponse.onSuccess(medicineService.checkAllConflicts(itemSeq, email));
    }



    // 병용금기
    @Operation(summary = "병용금기", description = "헤더의 itemSeq약물 번호로 해당 약물의 병용금기 약물 리턴")
    @GetMapping("/usjnt-taboo")
    public String UsjntTaboocallapi(@RequestParam String itemSeq) throws IOException {
        StringBuilder sb = new StringBuilder();
        //병용금기 정보조회
        String urlbyeongyong = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getUsjntTabooInfoList03?" +
                "serviceKey="+ serviceKey +
                //받아올 페이지 수
                "&pageNo=10" +
                //한 페이지당 받을 약물 갯수
                "&numOfRows=10" +
                "&type=json" +
                //"&typeName=병용금기" + -> 한글로 인코딩
                "&typeName=" + URLEncoder.encode("병용금기","UTF-8")+
                //임의값
                //"&itemSeq=201405281" ;
                //추후 프론트에서 받아올 시 itemSeq requestparam
                "&itemSeq="+itemSeq ;
        URL url = new URL(urlbyeongyong);
        log.info(url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            sb.append(returnLine+"\n\r");
        }
        urlConnection.disconnect();
        String json = sb.toString(); // Replace with actual JSON

        //ApiService apiService = new ApiService();
        String resultJson = apiService.usjntTabooProcessApiItems(json);

        //return sb.toString();
            System.out.println(resultJson);
            return resultJson;


    }
    //효능군 중복
    @Operation(summary = "효능군 중복", description = "헤더의 itemSeq약물 번호로 해당 약물의 효능군 중복 약물 리턴")
    @GetMapping("/efcy-dplct")
    public String EfcyDplctcallapi(@RequestParam String itemSeq) throws IOException {
        StringBuilder sb = new StringBuilder();
        //병용금기 정보조회
        String urlEfcy = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getEfcyDplctInfoList03?" +
                "serviceKey="+ serviceKey +
                //받아올 페이지 수
                "&pageNo=1" +
                //한 페이지당 받을 약물 갯수
                "&numOfRows=10" +
                "&type=json" +
                //"&typeName=병용금기" + -> 한글로 인코딩
                "&typeName=" + URLEncoder.encode("효능군중복","UTF-8")+
                //임의값
                //"&itemSeq=199102092";
                //추후 프론트에서 받아올 시 itemSeq requestparam
                "&itemSeq="+itemSeq ;
        URL url = new URL(urlEfcy);
        log.info(url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            sb.append(returnLine+"\n\r");
            log.info(sb.toString());
        }
        urlConnection.disconnect();
        String json = sb.toString(); // Replace with actual JSON

        //EfcyApiService efcyApiService = new EfcyApiService();
        String resultJson = efcyApiService.efcyDplctProcessApiItems(json);

        //return sb.toString();
            System.out.println(resultJson);
        return resultJson;



    }

    @Operation(summary = "약국 병원 이름 주소 전화번호 ", description = "약국 병원 이름 주소 전화번호 리턴")
    @GetMapping("get-phone-address")
    public ApiResponse<PhoneAddresses> getPhoneNumber(@RequestParam String itemSeq) throws IOException {

        PhoneAddresses phoneAddresses = apiService.getPhoneAddresses(itemSeq);
        return ApiResponse.onSuccess(phoneAddresses);


    }

    @DeleteMapping("/conflict-remove")
    public ApiResponse<Void> deleteConflict(@RequestHeader(value = "Authorization", required = true) String token,
                                         @RequestParam String itemSeq) throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String email = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtService.validateToken(jwtToken)) {
                email = jwtService.extractEmail(jwtToken);

            } else {
                log.info("Invalid JWT");
                throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
            }
        }

        //충돌 실제 약물 넣어놓을 시에 추가
        apiService.delete(itemSeq);
            return ApiResponse.onSuccess(null);
        }
    }


