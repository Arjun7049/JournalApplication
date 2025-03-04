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

import com.journal.entity.JournalEntry;
import com.journal.service.JournalService;

@RestController
@RequestMapping("/journal")
public class JournalController {
	
	@Autowired
	private JournalService journalService;

	@PostMapping("/{username}")
	public ResponseEntity<JournalEntry> saveEntry(@PathVariable String username, @RequestBody JournalEntry journalEntry) {
		
		try {
			return new ResponseEntity<>(journalService.saveEntry(username,journalEntry),HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<?> getAllEntriesOfUser(@PathVariable String username){
		try {
			return new ResponseEntity<List<JournalEntry>>(journalService.getAllEntriesOfUser(username),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/{username}/id/{id}")
	public ResponseEntity<?>getEntry(@PathVariable ObjectId id){
		
		try {
			JournalEntry entry = journalService.getEntry(id);
			if(entry!=null)
				return new ResponseEntity<>(entry,HttpStatus.OK);
			
			return new ResponseEntity<>("No entry found!!",HttpStatus.BAD_REQUEST);
			
			
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/{username}/id/{id}")
	public ResponseEntity<?> updateEntity(@PathVariable String username, @PathVariable ObjectId id, @RequestBody JournalEntry journalEntry){
		try {
			JournalEntry oldEntry= journalService.getEntry(id);
			
			if(oldEntry!=null) {
				oldEntry.setContent(journalEntry.getContent()!=null && journalEntry.getContent().strip()!=""?journalEntry.getContent():oldEntry.getContent());
				oldEntry.setTitle(journalEntry.getTitle()!=null && journalEntry.getTitle().strip()!=""?journalEntry.getTitle():oldEntry.getTitle());
				return new ResponseEntity<>(journalService.saveEntry(oldEntry),HttpStatus.OK);
			}
			return new ResponseEntity<>("No entry found",HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@DeleteMapping("/{username}/id/{id}")
	public ResponseEntity<String>deleteEntry(@PathVariable String username, @PathVariable ObjectId id){
		
		try {
			JournalEntry entry= journalService.getEntry(id);
			if(entry!=null)
				return new ResponseEntity<>(journalService.deleteEntry(username,id),HttpStatus.OK);
			
			return new ResponseEntity<>("No entry found!!",HttpStatus.BAD_REQUEST);
			
			
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
