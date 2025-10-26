package com.example.Pill_Mate_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@ConfigurationPropertiesScan("com.example.Pill_Mate_Backend")
@EnableFeignClients(basePackages = "com.example.Pill_Mate_Backend")
@SpringBootApplication(scanBasePackages = "com.example.Pill_Mate_Backend")
public class PillMateBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PillMateBackendApplication.class, args);
	}

}
