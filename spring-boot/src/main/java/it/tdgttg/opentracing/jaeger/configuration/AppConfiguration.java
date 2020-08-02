package it.tdgttg.opentracing.jaeger.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
	
	@Value("hosts")
	private String[] hosts;

	public String[] getHosts() {
		return hosts;
	}
	
}
