package com.example.Pill_Mate_Backend.domain.oauth2.feign;

import com.example.Pill_Mate_Backend.domain.oauth2.dto.ApplePublicKeyResponse;
import feign.Retryer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleAuthClient", url = "${apple.auth.public-key-url}")
public interface AppleAuthClient {
    @GetMapping
    public abstract ApplePublicKeyResponse getAppleAuthPublicKey();

    @Configuration
    public class FeignRetryConfig {
        @Bean
        public Retryer retryer() {
            return new Retryer.Default(1000, 1500, 1);
        }
    }
}
