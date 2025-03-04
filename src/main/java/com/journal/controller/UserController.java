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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.entity.User;
import com.journal.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<User> saveEntity(@RequestBody User user) {
		
		try {
			return new ResponseEntity<>(userService.saveEntry(user),HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
	}
	
	@GetMapping
	public ResponseEntity<?> getAllEntries(){
		try {
			return new ResponseEntity<List<User>>(userService.getAllEntries(),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?>getEntry(@PathVariable ObjectId id){
		
		try {
			User entry = userService.getEntry(id);
			if(entry!=null)
				return new ResponseEntity<>(entry,HttpStatus.OK);
			
			return new ResponseEntity<>("No user entry found!!",HttpStatus.BAD_REQUEST);
			
			
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/{username}")
	public ResponseEntity<?> updateEntity(@PathVariable String username, @RequestBody User user){
		try {
			User oldEntry= userService.getUserByUsername(username);
			
			if(oldEntry!=null) {
				oldEntry.setUsername(user.getUsername()!=null && user.getUsername().strip()!=""?user.getUsername():oldEntry.getUsername());
				oldEntry.setPassword(user.getPassword()!=null && user.getPassword().strip()!=""?user.getPassword():oldEntry.getPassword());
				return new ResponseEntity<>(userService.saveEntry(oldEntry),HttpStatus.OK);
			}
			return new ResponseEntity<>("No entry found",HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>deleteEntry(@PathVariable ObjectId id){
		
		try {
			User entry= userService.getEntry(id);
			if(entry!=null)
				return new ResponseEntity<>(userService.deleteEntry(id),HttpStatus.OK);
			
			return new ResponseEntity<>("No entry found!!",HttpStatus.BAD_REQUEST);
			
			
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	

}
