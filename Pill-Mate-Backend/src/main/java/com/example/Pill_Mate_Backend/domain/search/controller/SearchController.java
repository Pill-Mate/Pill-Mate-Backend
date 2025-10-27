package com.example.Pill_Mate_Backend.domain.search.controller;

import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.domain.search.dto.SearchPillResponseDto;
import com.example.Pill_Mate_Backend.domain.search.service.SearchService;
import com.example.Pill_Mate_Backend.global.common.ApiResponse;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;


@Slf4j
@RestController
@RequestMapping("/api/v2/search")
@RequiredArgsConstructor
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    JwtService jwtService;

    @Operation(summary = "약물 상세 검색", description = "약물 식별 번호로 약물 상세를 검색합니다.")
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<SearchPillResponseDto>> searchPillDetail(@RequestHeader(value = "Authorization", required = true) String token,
                                                                               @RequestParam Long itemSeq){
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
        return ResponseEntity.ok(ApiResponse.onSuccess(searchService.searchPillDetail(itemSeq,email)));
    }

}
