package com.journal.controller;

import java.util.List;

import com.journal.exception.UserException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.entity.User;
import com.journal.service.UserService;
import com.journal.util.UserUtill;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/create-admin")
	public ResponseEntity<?> createAdmin(@RequestBody User user) throws Exception {
		log.info("create admin request received {}",user.getUsername());
		User adminUser = userService.createAdmin(user);
		return new ResponseEntity<>(adminUser,HttpStatus.CREATED);
	}
	@GetMapping
	public ResponseEntity<?> getAllEntries() throws UserException{
		String username = UserUtill.getLoggedInUser();
		log.info("get all users request received from {}",username);
		return new ResponseEntity<List<User>>(userService.getAllEntries(), HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getEntry(@PathVariable ObjectId id) throws UserException {
		log.info("get user request received for {}", id);
		User user = userService.getEntry(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	@DeleteMapping
	public ResponseEntity<String> deleteUserByUsername() throws UserException {
		String username = UserUtill.getLoggedInUser();
		log.info("delete user request received for {}", username);
		return new ResponseEntity<>(userService.deleteByUsername(username), HttpStatus.OK);
	}
}
