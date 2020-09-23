package com.capg.ow;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		OnlineWalletAkhilApplication.class,
		Jsr310JpaConverters.class
})
public class OnlineWalletAkhilApplication {
	
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedHeaders("*").allowedOrigins("*").allowedMethods("*")
				.allowCredentials(true);
			}
		};
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlineWalletAkhilApplication.class, args);
	}
}
