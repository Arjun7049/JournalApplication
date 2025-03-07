package com.journal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.dto.Mail;
import com.journal.entity.User;
import com.journal.repository.UserRepositoryImpl;
import com.journal.service.MailSenderImpl;

@RestController
@RequestMapping("/public")
public class PublicController {

	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	private MailSenderImpl mailSender;
	
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
}
