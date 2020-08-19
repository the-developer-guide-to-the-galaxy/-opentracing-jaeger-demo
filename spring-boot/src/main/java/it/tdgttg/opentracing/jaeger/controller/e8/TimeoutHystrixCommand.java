package it.tdgttg.opentracing.jaeger.controller.e8;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class TimeoutHystrixCommand<T> extends HystrixCommand<T>{

	Callable<T> delegate;
	
	public TimeoutHystrixCommand(Callable<T> delegate, Integer timeoutInMilliseconds, String groupKey) {
		super(getHystrixConfiguration(timeoutInMilliseconds, groupKey));
		this.delegate = delegate;
	}
	
	@Override
    protected T run() throws Exception {
        return this.delegate.call();
    }
	
	private static HystrixCommand.Setter getHystrixConfiguration(Integer timeoutInMilliseconds, String groupKey) {
        HystrixCommandGroupKey commandGroupKey = HystrixCommandGroupKey.Factory.asKey(groupKey);
        HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(commandGroupKey);
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(timeoutInMilliseconds);
        config.andCommandPropertiesDefaults(commandProperties);
        return config;
    }
	
	public Callable<T> asCallable(){
		return ()-> this.execute();
	}
	
	public Runnable asRunnable(){
		return ()-> this.execute();
	}
	
	public Supplier<T> asSupplier(){
		return ()-> this.execute();
	}
	
	
}
