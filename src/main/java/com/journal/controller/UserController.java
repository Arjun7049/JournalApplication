package com.journal.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.entity.User;
import com.journal.service.UserService;
import com.journal.util.UserUtill;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

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

	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody User user) {

		try {
			String username = UserUtill.getLoggedInUser();
			User oldEntry = userService.getUserByUsername(username);

			oldEntry.setUsername(user.getUsername() != null && user.getUsername().strip() != "" ? user.getUsername()
					: oldEntry.getUsername());
			oldEntry.setPassword(user.getPassword() != null && user.getPassword().strip() != "" ? user.getPassword()
					: oldEntry.getPassword());
			return new ResponseEntity<>(userService.saveUserEntry(oldEntry), HttpStatus.OK);

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
