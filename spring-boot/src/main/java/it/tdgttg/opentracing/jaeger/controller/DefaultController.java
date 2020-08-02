package it.tdgttg.opentracing.jaeger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
	
	@GetMapping("greetings")
	public String greetings() {
		return "Greetings from GDI!";
	}

	
}
