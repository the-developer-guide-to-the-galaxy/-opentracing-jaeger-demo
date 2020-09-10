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
	
	private AmqpConfiguration amqp = new AmqpConfiguration();

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

		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public void setPort(Integer port) {
			this.port = port;
		}
		
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

	public void setHosts(String[] hosts) {
		this.hosts = hosts;
	}

	public void setHystrixStrategyEnabled(Boolean hystrixStrategyEnabled) {
		this.hystrixStrategyEnabled = hystrixStrategyEnabled;
	}

	public AmqpConfiguration getAmqp() {
		return amqp;
	}

	public void setAmqp(AmqpConfiguration amqp) {
		this.amqp = amqp;
	}
	
	
	
	

}
