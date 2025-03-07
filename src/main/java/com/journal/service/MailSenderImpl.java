package com.journal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.journal.dto.Mail;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailSenderImpl {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendMail(Mail mailContent) {
		
		try {
			SimpleMailMessage mail= new SimpleMailMessage();
			mail.setTo(mailContent.getSentTo());
			mail.setSubject(mailContent.getSubject());
			mail.setText(mailContent.getBody());
			javaMailSender.send(mail);
			
		}catch(Exception e) {
			log.error("Error occurred while sending mail ",e);
			throw e;
		}
		
	}
}
