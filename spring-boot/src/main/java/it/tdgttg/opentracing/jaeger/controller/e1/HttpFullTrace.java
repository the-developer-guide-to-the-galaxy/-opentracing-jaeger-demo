package it.tdgttg.opentracing.jaeger.controller.e1;

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
	path="http-full-trace", 
	produces=MediaType.APPLICATION_JSON_VALUE
)
public class HttpFullTrace {

	@Autowired
	IAppConfiguration appConfiguration;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("greetings")
	public String greetings() {
		return "Greetings from GDI!";
	}

	@GetMapping
	public ResultDTO httpFullTrace() {
		String[] hosts = appConfiguration.getHosts();
		String firstHost = hosts[0];
		String secondHost = hosts[1];
		restTemplate.getForObject(firstHost + "", String.class);
		ResultDTO response = restTemplate.getForObject(secondHost + "/random", ResultDTO.class);
		return new ResultDTO(response.getValue());
	}

}


