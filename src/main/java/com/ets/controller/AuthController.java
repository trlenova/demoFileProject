package com.ets.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ets.services.FileService;
import com.ets.services.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JwtService jwtService;
	
	
	@GetMapping("/generateToken")
	public ResponseEntity<String> generateToken() {
		return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(new User("develop_user","123456",Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))).toString());

	}

}
