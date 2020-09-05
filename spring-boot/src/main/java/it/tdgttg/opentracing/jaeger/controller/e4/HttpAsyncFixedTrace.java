package it.tdgttg.opentracing.jaeger.controller.e4;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import io.opentracing.util.GlobalTracer;
import it.tdgttg.opentracing.jaeger.configuration.IAppConfiguration;
import it.tdgttg.opentracing.jaeger.dto.ResultDTO;

@RestController
@RequestMapping(path = "http-async-fixed-trace", produces = MediaType.APPLICATION_JSON_VALUE)
public class HttpAsyncFixedTrace {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpAsyncFixedTrace.class);

	@Autowired
	IAppConfiguration appConfiguration;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping
	public ResultDTO index() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Tracer tracer = GlobalTracer.get();
		TracedExecutorService tracedExecutorService = new TracedExecutorService(executorService, tracer);
		String[] hosts = appConfiguration.getHosts();
		String firstHost = hosts[0];
		String secondHost = hosts[1];
		Runnable notifyRunnable = notify(firstHost);
		Supplier<ResultDTO> randomSupply = random(secondHost);
		CompletableFuture<?> notifyFuture = CompletableFuture.runAsync(notifyRunnable, tracedExecutorService);
		CompletableFuture<ResultDTO> randomFuture = CompletableFuture.supplyAsync(randomSupply, tracedExecutorService);
		CompletableFuture.allOf(notifyFuture, randomFuture).join();
		try {
			notifyFuture.get();
			ResultDTO resultDTO;
			resultDTO = randomFuture.get();
			return new ResultDTO(resultDTO.getValue());
		} catch (InterruptedException interruptedException) {
			throw new RuntimeException(interruptedException) ;
		} catch (ExecutionException executionException) {
			throw new RuntimeException(executionException.getCause()) ;
		}
	}

	private Supplier<ResultDTO> random(String secondHost) {
		return new Supplier<ResultDTO>() {
			@Override
			public ResultDTO get() {
				//TODO: use path join instead string concatenation
				ResultDTO response = restTemplate.getForObject(secondHost + "/random", ResultDTO.class);
				return response;
			}
		};
	}

	private Runnable notify(String firstHost) {
		return new Runnable() {
			@Override
			public void run() {
				//TODO: use path join instead string concatenation
				String norifyResult = restTemplate.getForObject(firstHost + "/notify", String.class);
				LOGGER.info(norifyResult);
			}
		};
	}

}
