package com.icbt.onlineappointmentschedulingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OnlineAppointmentSchedulingApplication {

	@Configuration
	@EnableWebMvc
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/api/**")
					.allowedOrigins("http://localhost:3000","http://localhost:8080")
					.allowedMethods("GET", "POST", "PUT", "DELETE")
					.allowCredentials(true);  // Allow sending cookies and other credentials
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(OnlineAppointmentSchedulingApplication.class, args);
	}

}
