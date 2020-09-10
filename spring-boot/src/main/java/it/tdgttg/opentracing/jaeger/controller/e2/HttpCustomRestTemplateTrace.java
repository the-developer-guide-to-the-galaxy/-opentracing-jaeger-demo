package it.tdgttg.opentracing.jaeger.controller.e2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import it.tdgttg.opentracing.jaeger.configuration.IAppConfiguration;
import it.tdgttg.opentracing.jaeger.dto.ResultDTO;

@RestController
@RequestMapping(
	path="http-trace-custom-rest-template", 
	produces=MediaType.APPLICATION_JSON_VALUE
)
public class HttpCustomRestTemplateTrace {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpCustomRestTemplateTrace.class);

	@Autowired
	IAppConfiguration appConfiguration;

	RestTemplate restTemplate = new RestTemplate();

	@GetMapping
	public ResultDTO index() {
		String[] hosts = appConfiguration.getHosts();
		String firstHost = hosts[0];
		String secondHost = hosts[1];
		//TODO: use path join instead string concatenation
		String norifyResult = restTemplate.getForObject(firstHost + "/notify", String.class);
		LOGGER.info(norifyResult);
		//TODO: use path join instead string concatenation
		ResultDTO response = restTemplate.getForObject(secondHost + "/random", ResultDTO.class);
		return new ResultDTO(response.getValue());
	}

}


