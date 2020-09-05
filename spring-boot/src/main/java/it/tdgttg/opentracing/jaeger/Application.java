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

import it.tdgttg.opentracing.jaeger.configuration.IAppConfiguration;
	
@SpringBootApplication
public class Application {
	
	private IAppConfiguration appConfiguration;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.setReadTimeout(Duration.ofSeconds(25))
			.build();
	}
	
	@Bean
	public ConnectionFactory connectionFactory() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername(appConfiguration.getAmqpUsername());
		connectionFactory.setPassword(appConfiguration.getAmqpPassword());
		connectionFactory.setHost(appConfiguration.getAmqpHost());
		connectionFactory.setPort(appConfiguration.getAmqpPort());
		return connectionFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
