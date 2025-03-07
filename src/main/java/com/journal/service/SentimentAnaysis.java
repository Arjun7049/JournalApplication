package com.journal.service;

import org.springframework.stereotype.Service;

@Service
public class SentimentAnaysis {

	public String getSentiment(String content) {
		return "You were Positive last week";
	}
}
