package com.example.Pill_Mate_Backend.domain.conflict.controller;

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
    public String callapi() throws IOException {
        StringBuilder sb = new StringBuilder();
        //병용금기 정보조회
        String urlbyeongyong = "http://apis.data.go.kr/1471000/DURPrdlstInfoService03/getUsjntTabooInfoList03?" +
                "serviceKey=UY683TnxM3KUyQNIcQA5uiJSDCQvR9SYSlBjSqiScBYB2RE/FUWFJ/Q7vpIx9pr12crjptoRu/MX5RKnFzm/SQ=="+
                "&pageNo=1" +
                "&numOfRows=3" +
                "&type=json" +
                //"&typeName=병용금기" +
                "&typeName=" + URLEncoder.encode("병용금기","UTF-8")+
                "&itemSeq=201405281" ;
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

        return sb.toString();

    }

}
