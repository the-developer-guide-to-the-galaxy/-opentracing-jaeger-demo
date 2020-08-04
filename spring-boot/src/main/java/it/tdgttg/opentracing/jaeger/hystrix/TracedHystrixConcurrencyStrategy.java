package it.tdgttg.opentracing.jaeger.hystrix;

import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

import io.opentracing.Tracer;

public class TracedHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TracedHystrixConcurrencyStrategy.class);
	
	private static TracedHystrixConcurrencyStrategy INSTANCE = null;
	
	private HystrixConcurrencyStrategy delegateStrategy;
    private Tracer tracer;

    public static synchronized boolean registerIfAbsent(final Tracer tracer) {
    	if(INSTANCE != null) {
    		LOGGER.warn("trying to register multiple strategy instances");
    		return false;
    	}
    	TracedHystrixConcurrencyStrategy instance = new TracedHystrixConcurrencyStrategy(tracer);
    	try {
            HystrixPlugins hystrixPlugins = HystrixPlugins.getInstance();
            instance.delegateStrategy = hystrixPlugins.getConcurrencyStrategy();
            if (instance.delegateStrategy instanceof TracedHystrixConcurrencyStrategy) {
            	INSTANCE = (TracedHystrixConcurrencyStrategy) instance.delegateStrategy;
                return true;
            }
            HystrixCommandExecutionHook commandExecutionHook = hystrixPlugins.getCommandExecutionHook();
            HystrixEventNotifier eventNotifier = hystrixPlugins.getEventNotifier();
            HystrixMetricsPublisher metricsPublisher = hystrixPlugins.getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy = hystrixPlugins.getPropertiesStrategy();
            HystrixPlugins.reset();
            hystrixPlugins.registerConcurrencyStrategy(instance);
            hystrixPlugins.registerCommandExecutionHook(commandExecutionHook);
            hystrixPlugins.registerEventNotifier(eventNotifier);
            hystrixPlugins.registerMetricsPublisher(metricsPublisher);
            hystrixPlugins.registerPropertiesStrategy(propertiesStrategy);
            INSTANCE = instance;
        	return true;
        } catch (Exception ex) {
        	String className = TracedHystrixConcurrencyStrategy.class.getName();
			String errorMessage = "Failed to register " + className + ", to HystrixPlugins";
			LOGGER.error(errorMessage, ex);
			return false;
        }
    }

    private TracedHystrixConcurrencyStrategy(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        if (callable instanceof TracedHystrixCallable) {
            return callable;
        }
        Callable<T> delegateCallable = null;
        if(this.delegateStrategy == null) {
        	delegateCallable = callable;
        }
        else {
        	delegateCallable = this.delegateStrategy.wrapCallable(callable);
        }
        if (delegateCallable instanceof TracedHystrixCallable) {
            return delegateCallable;
        }
        ScopeManager scopeManager = tracer.scopeManager();
		if (scopeManager.activeSpan() == null) {
            return delegateCallable;
        }
        Span activeSpan = tracer.activeSpan();
		return new TracedHystrixCallable<T>(delegateCallable, scopeManager, activeSpan);
    }

    private static class TracedHystrixCallable<S> implements Callable<S> {
    	
        private final Callable<S> delegateCallable;
        private ScopeManager scopeManager;
        private Span span;

        public TracedHystrixCallable(Callable<S> delegate, ScopeManager scopeManager, Span span) {
            if (span == null || delegate == null || scopeManager == null) {
                throw new NullPointerException();
            }
            this.delegateCallable = delegate;
            this.scopeManager = scopeManager;
            this.span = span;
        }

        @Override
        public S call() throws Exception {
            try (Scope scope = scopeManager.activate(span)) {
                return delegateCallable.call();
            }
        }
    }
}
