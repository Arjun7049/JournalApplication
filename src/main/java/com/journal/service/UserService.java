package com.journal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import com.journal.entity.User;
import com.journal.repository.UserRepository;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User saveEntry(User user) {
		return userRepository.save(user);
	}


	public List<User> getAllEntries() {
		return userRepository.findAll();
	}


	public User getEntry(ObjectId id) {	
		return userRepository.findById(id).orElse(null);
	}
	
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}


	public String deleteEntry(ObjectId id) {
		userRepository.deleteById(id);
		return "User Entry deleted successfully";
	}
	
}
