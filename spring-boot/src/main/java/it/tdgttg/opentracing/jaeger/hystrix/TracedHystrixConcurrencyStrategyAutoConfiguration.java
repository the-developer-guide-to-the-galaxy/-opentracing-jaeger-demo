package it.tdgttg.opentracing.jaeger.hystrix;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import io.opentracing.Tracer;
import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration;

@Configuration
@ConditionalOnBean(Tracer.class)
@AutoConfigureAfter(TracerAutoConfiguration.class)
public class TracedHystrixConcurrencyStrategyAutoConfiguration {

	@Autowired
	Tracer tracer;
	
	@PostConstruct
	public void registerTracedHystrixConcurrencyStrategy() {
		//TracedHystrixConcurrencyStrategy.registerIfAbsent(tracer);
	}
}
