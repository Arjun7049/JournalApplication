package com.journal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.entity.User;
import com.journal.service.UserService;

@RestController
@Slf4j
@RequestMapping("/public")
public class RegisterUser {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<User> saveEntity(@RequestBody User user) throws Exception {
		log.info("Registering user: {}", user);
			User response = userService.saveUserEntry(user);
			return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
}
