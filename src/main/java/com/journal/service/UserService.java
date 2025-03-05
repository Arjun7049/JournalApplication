package com.journal.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journal.entity.User;
import com.journal.repository.UserRepository;

@Component
@Transactional
public class UserService {
	
	
	private static final PasswordEncoder PASSWORD_ENCODER= new BCryptPasswordEncoder();

	@Autowired
	private UserRepository userRepository;
	
	public User saveEntry(User user) {
		return userRepository.save(user);
	}
	
	public User saveUserEntry(User user) {
		user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
		user.getUserRoles().add("USER");
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
	public String deleteByUsername(String username) {
		userRepository.deleteByUsername(username);
		return "User Entry deleted successfully";
	}
	
}
