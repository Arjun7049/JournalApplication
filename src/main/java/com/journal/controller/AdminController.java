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

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/create-admin")
	public ResponseEntity<?> createAdmin(@RequestBody User user){
		try {
		User existingUser= userService.getUserByUsername(user.getUsername());
		User updatedUser;
		if(existingUser!=null) {
			existingUser.getUserRoles().add("ADMIN");
			updatedUser=	userService.saveEntry(existingUser);
			return new ResponseEntity<>(updatedUser,HttpStatus.CREATED);
		}
		user.getUserRoles().add("ADMIN");
		updatedUser=userService.saveUserEntry(user);
		return new ResponseEntity<>(updatedUser,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@GetMapping
	public ResponseEntity<?> getAllEntries() {
		System.out.println("Get all user method called");
		try {
			return new ResponseEntity<List<User>>(userService.getAllEntries(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEntry(@PathVariable ObjectId id) {

		try {
			User entry = userService.getEntry(id);
			if (entry != null)
				return new ResponseEntity<>(entry, HttpStatus.OK);

			return new ResponseEntity<>("No user entry found!!", HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DeleteMapping
	public ResponseEntity<String> deleteUserByUsername() {
		String username = UserUtill.getLoggedInUser();
		try {
			String reposne= userService.deleteByUsername(username);
			return new ResponseEntity<>(reposne, HttpStatus.OK);


		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
