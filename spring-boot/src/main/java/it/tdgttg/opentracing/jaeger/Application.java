package it.tdgttg.opentracing.jaeger;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.ConnectionFactory;
	
@SpringBootApplication
public class Application {
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.setReadTimeout(Duration.ofSeconds(25))
			.build();
	}
	
	@Bean
	public ConnectionFactory connectionFactory() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setHost("192.168.0.31");
		connectionFactory.setPort(5672);
		return connectionFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
