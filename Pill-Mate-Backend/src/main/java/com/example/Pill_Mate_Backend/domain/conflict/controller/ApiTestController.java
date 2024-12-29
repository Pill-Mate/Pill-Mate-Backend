package com.example.Pill_Mate_Backend.domain.conflict.controller;

import com.example.Pill_Mate_Backend.domain.conflict.service.ApiService;
import com.example.Pill_Mate_Backend.domain.conflict.service.EfcyApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiTestController {
    @GetMapping("/api1")
    public String UsjntTaboocallapi(@RequestParam String itemSeq) throws IOException {
        StringBuilder sb = new StringBuilder();
        //병용금기 정보조회
        String urlbyeongyong = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getUsjntTabooInfoList03?" +
                "serviceKey=UY683TnxM3KUyQNIcQA5uiJSDCQvR9SYSlBjSqiScBYB2RE/FUWFJ/Q7vpIx9pr12crjptoRu/MX5RKnFzm/SQ=="+
                //받아올 페이지 수
                "&pageNo=10" +
                //한 페이지당 받을 약물 갯수
                "&numOfRows=10" +
                "&type=json" +
                //"&typeName=병용금기" + -> 한글로 인코딩
                "&typeName=" + URLEncoder.encode("병용금기","UTF-8")+
                //임의값
                "&itemSeq=201405281" ;
                //추후 프론트에서 받아올 시 itemSeq requestparam
                //"&itemSeq="+itemSeq ;
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

        ApiService apiService = new ApiService();
        String resultJson = apiService.usjntTabooProcessApiItems(json);

        //return sb.toString();
        return resultJson;

    }
    @GetMapping("/api2")
    public String EfcyDplctcallapi(@RequestParam String itemSeq) throws IOException {
        StringBuilder sb = new StringBuilder();
        //병용금기 정보조회
        String urlEfcy = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getEfcyDplctInfoList03?" +
                "serviceKey=UY683TnxM3KUyQNIcQA5uiJSDCQvR9SYSlBjSqiScBYB2RE/FUWFJ/Q7vpIx9pr12crjptoRu/MX5RKnFzm/SQ=="+
                //받아올 페이지 수
                "&pageNo=1" +
                //한 페이지당 받을 약물 갯수
                "&numOfRows=10" +
                "&type=json" +
                //"&typeName=병용금기" + -> 한글로 인코딩
                "&typeName=" + URLEncoder.encode("효능군중복","UTF-8")+
                //임의값
                "&itemSeq=199102092";
                //추후 프론트에서 받아올 시 itemSeq requestparam
                //"&itemSeq="+itemSeq ;
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

        EfcyApiService efcyApiService = new EfcyApiService();
        String resultJson = efcyApiService.efcyDplctProcessApiItems(json);

        //return sb.toString();
        return resultJson;


    }

}
