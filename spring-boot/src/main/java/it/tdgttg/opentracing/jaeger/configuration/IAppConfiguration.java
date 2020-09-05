package it.tdgttg.opentracing.jaeger.configuration;

public interface IAppConfiguration {
	
	String[] getHosts();
	
	Boolean getHystrixStrategyEnabled();
	
	String getAmqpUsername();

	String getAmqpPassword();

	String getAmqpHost();
	
	Integer getAmqpPort();
}
