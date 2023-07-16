package com.ets.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class FileController {
	
	@GetMapping("/")
	public  String index() {
		return "Greetings from Spring Boot!";
	}

}
