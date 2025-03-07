package com.journal.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.journal.dto.Mail;
import com.journal.entity.User;
import com.journal.repository.UserRepositoryImpl;
import com.journal.service.MailSenderImpl;
import com.journal.service.SentimentAnaysis;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserScheduler {

	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	private MailSenderImpl mailSenderImpl;
	
	@Autowired
	private SentimentAnaysis sentimentAnaysis;
	
	@Value("${mail.sentiment-index}")
	private String subject;
	
//	@Scheduled(cron = "*/10 * * * * *") 
	@Scheduled(cron = "0 0 9 ? * SUN")
	public void fetchUserAndSendSAMail() {
		
	 List<User>users = userRepositoryImpl.getUserForSentimentAnalysis();
	 
	 for(User user : users) {
		List<String>content=  user.getJournalEntries().stream()
				.filter(x->x.getTimestamp().isAfter(LocalDateTime.now().minus(7,ChronoUnit.DAYS)))
				.map(x->x.getContent()).collect(Collectors.toList());
		String entry= String.join(" ", content);
		String sentiment= sentimentAnaysis.getSentiment(entry);
		mailSenderImpl.sendMail(new Mail(user.getEmail(),subject,sentiment));
		log.info("sentiment status :",sentiment);
		
	 }
	}
}
