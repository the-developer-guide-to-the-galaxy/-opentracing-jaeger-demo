package it.tdgttg.opentracing.jaeger.configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class AppConfiguration implements IAppConfiguration {

	private String[] hosts;
	
	private Boolean hystrixStrategyEnabled;
	
	private AmqpConfiguration amqp;

	@Override
	public String[] getHosts() {
		return hosts;
	}

	@Override
	public Boolean getHystrixStrategyEnabled() {
		return hystrixStrategyEnabled;
	}

	protected static class AmqpConfiguration{
		
		@NotBlank
		protected String username;

		@NotBlank
		protected String password;

		@NotBlank
		protected String host;

		@NotNull
		@Positive
		protected Integer port;
		
	}
	
	@Override
	public String getAmqpUsername() {
		return amqp.username;
	}

	@Override
	public String getAmqpPassword() {
		return amqp.password;
	}

	@Override
	public String getAmqpHost() {
		return amqp.host;
	}

	@Override
	public Integer getAmqpPort() {
		return amqp.port;
	}
	

}
