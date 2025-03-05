package com.journal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.repository.JournalRepository;
import com.journal.repository.UserRepository;

@Component
@Transactional
public class JournalService {
	
	@Autowired
	private JournalRepository journalRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public JournalEntry saveEntry(String username, JournalEntry journalEntry) {
		User user= userRepository.findByUsername(username);
		journalEntry.setTimestamp(LocalDateTime.now());
		 JournalEntry savedEntry= journalRepository.save(journalEntry);
		 user.getJournalEntries().add(savedEntry);
		 userRepository.save(user);
		return savedEntry;
	}
	
	public JournalEntry saveEntry(JournalEntry journalEntry) {
		 JournalEntry savedEntry= journalRepository.save(journalEntry);
		return savedEntry;
	}
	
	


	public List<JournalEntry> getAllEntriesOfUser(String username) {
		User user = userRepository.findByUsername(username);		
		return user.getJournalEntries();
	}


	public JournalEntry getEntry(ObjectId id) {	
		return journalRepository.findById(id).orElse(null);
	}


	public String deleteEntry(String username, ObjectId id) {
		
		User user = userRepository.findByUsername(username);
		user.getJournalEntries().removeIf(x->x.getId().equals(id));
		userRepository.save(user);
		journalRepository.deleteById(id);
		return "Entry deleted successfully";
	}

}
