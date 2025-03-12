package com.journal.controller;

import java.util.List;

import com.journal.service.UserDetailsServiceImpl;
import com.journal.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.dto.JwtResponse;
import com.journal.dto.Mail;
import com.journal.entity.User;
import com.journal.repository.UserRepositoryImpl;
import com.journal.service.MailSenderImpl;

@RestController
@Slf4j
@RequestMapping("/public")
public class PublicController {

	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	private MailSenderImpl mailSender;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JWTUtil jwtUtil;
	
	@GetMapping
	public List<User> getUser(){
		return userRepositoryImpl.getUserForSentimentAnalysis();
	}
	
	@PostMapping
	public ResponseEntity<String> sendMail(@RequestBody Mail mail){
		try {
			mailSender.sendMail(mail);
			return new ResponseEntity<String>("Mail Sent successfully !!", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>("Something wrong!!", HttpStatus.OK);
		}
		
	}

	@PostMapping("/signup")
	public void signup(@RequestBody User user){

	}
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user){
		try{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
			UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
			String token = jwtUtil.generateToken(userDetails.getUsername());
			
			JwtResponse jwtResponse = new JwtResponse();
			jwtResponse.setToken(token);
			return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
		}catch (Exception e){
			log.error("Exception occurred while createAuthenticationToken ", e);
			return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
		}
	}
}
