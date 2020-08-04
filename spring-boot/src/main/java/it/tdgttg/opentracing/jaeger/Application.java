package it.tdgttg.opentracing.jaeger;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
	
@SpringBootApplication
public class Application {
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.setReadTimeout(Duration.ofSeconds(25))
			.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
