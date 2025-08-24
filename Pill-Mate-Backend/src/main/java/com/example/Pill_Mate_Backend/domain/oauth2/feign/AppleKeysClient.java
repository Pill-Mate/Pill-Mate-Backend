package com.example.Pill_Mate_Backend.domain.oauth2.feign;

import com.example.Pill_Mate_Backend.domain.oauth2.dto.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleKeysClient", url = "${apple.base-url}")
public interface AppleKeysClient {
    @GetMapping("/auth/keys")
    ApplePublicKeyResponse getApplePublicKeys();
}