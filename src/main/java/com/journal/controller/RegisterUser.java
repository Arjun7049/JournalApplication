package com.journal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.dto.Mail;
import com.journal.entity.User;
import com.journal.service.MailSenderImpl;
import com.journal.service.UserService;

@RestController
@RequestMapping("/public")
public class RegisterUser {
	
	
	@Autowired
	private UserService userService;
	
	@Value("${mail.subject}")
	private String subject;
	
	@Value("${mail.content}")
	private String content;
	
	@Autowired
	private MailSenderImpl mailSenderImpl;
	
	@PostMapping("/register")
	public ResponseEntity<User> saveEntity(@RequestBody User user) {
		System.out.println("Post user method called");
		try {
			User respose = userService.saveUserEntry(user);
			Mail mail = new Mail(user.getEmail(),subject,content);
			mailSenderImpl.sendMail(mail);
			return new ResponseEntity<>(respose,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
	}

}
