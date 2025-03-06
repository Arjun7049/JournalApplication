package com.journal.controller;

import java.util.List;

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

import com.journal.UserUtill;
import com.journal.entity.User;
import com.journal.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	
	
	
	@PostMapping("/create-admin")
	public ResponseEntity<?> createAdmin(@RequestBody User user){
		log.info("create admin request recieved {}",user.getUsername());
		try {
		User existingUser= userService.getUserByUsername(user.getUsername());
		User updatedUser;
		if(existingUser!=null) {
			if(!existingUser.getUserRoles().contains("ADMIN")) {
				existingUser.getUserRoles().add("ADMIN");
				updatedUser=	userService.saveEntry(existingUser);
				return new ResponseEntity<>(updatedUser,HttpStatus.CREATED);
			}
			throw new Exception("User is already an ADMIN");
		}
		user.getUserRoles().add("ADMIN");
		updatedUser=userService.saveUserEntry(user);
		return new ResponseEntity<>(updatedUser,HttpStatus.CREATED);
		}catch(Exception e) {
			log.error(e.getMessage(),user,e);
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@GetMapping
	public ResponseEntity<?> getAllEntries() {
		String username = UserUtill.getLoggedInUser();
		log.info("get all users request recieved from {}",username);
		try {
			return new ResponseEntity<List<User>>(userService.getAllEntries(), HttpStatus.OK);
		} catch (Exception e) {
			log.error("error ocurred while getting user entry ",e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEntry(@PathVariable ObjectId id) {
		log.info("get user request recieved for {}", id);
		try {
			User entry = userService.getEntry(id);
			if (entry != null)
				return new ResponseEntity<>(entry, HttpStatus.OK);

			return new ResponseEntity<>("No user entry found!!", HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			log.error("error ocurred while getting user entry {}",id,e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DeleteMapping
	public ResponseEntity<String> deleteUserByUsername() {
		String username = UserUtill.getLoggedInUser();
		log.info("delete user request recieved for {}", username);
		try {
			String reposne= userService.deleteByUsername(username);
			return new ResponseEntity<>(reposne, HttpStatus.OK);


		} catch (Exception e) {
			log.error("error ocurred while deleting {}",username,e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
