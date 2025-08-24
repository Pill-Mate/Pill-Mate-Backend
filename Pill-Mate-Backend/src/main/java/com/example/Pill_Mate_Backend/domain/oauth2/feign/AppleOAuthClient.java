package com.example.Pill_Mate_Backend.domain.oauth2.feign;

import com.example.Pill_Mate_Backend.domain.oauth2.dto.AppleTokenRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "appleOAuthClient", url = "${apple.base-url}", configuration = FeignFormConfig.class)
public interface AppleOAuthClient {
    @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleTokenRes exchangeToken(@RequestBody Map<String, ?> form);

    @PostMapping(value = "/auth/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revoke(@RequestBody Map<String, ?> form);
}