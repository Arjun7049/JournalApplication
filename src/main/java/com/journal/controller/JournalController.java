package com.journal.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.journal.exception.UserException;
import lombok.extern.slf4j.Slf4j;
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

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.service.JournalService;
import com.journal.service.UserService;
import com.journal.util.UserUtill;

@RestController
@Slf4j
@RequestMapping("/journal")
public class JournalController {

	@Autowired
	private JournalService journalService;

	@PostMapping
	public ResponseEntity<JournalEntry> saveEntry(@RequestBody JournalEntry journalEntry) {
		log.info("Saving journal entry: {}", journalEntry);
		String username = UserUtill.getLoggedInUser();
		return new ResponseEntity<>(journalService.saveEntry(username, journalEntry), HttpStatus.CREATED);
	}
	@GetMapping
	public ResponseEntity<?> getAllEntriesOfUser() throws UserException {
		String username = UserUtill.getLoggedInUser();
		log.info("Fetching all journal entries for user{}",username);
		return new ResponseEntity<List<JournalEntry>>(journalService.getAllEntriesOfUser(username), HttpStatus.OK);
	}
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getEntry(@PathVariable ObjectId id) {
		String username = UserUtill.getLoggedInUser();
		log.info("Fetching journal entry with id: {} for user: {}", id, username);
		return new ResponseEntity<>(journalService.getUserEntry(username,id), HttpStatus.OK);
	}

	@PutMapping("/id/{id}")
	public ResponseEntity<?> updateEntity(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry) {
		log.info("Updating entry with id: {}", id);
		String username = UserUtill.getLoggedInUser();
		return new ResponseEntity<>(journalService.updateJournalEntry(username,id, journalEntry), HttpStatus.OK);
	}

	@DeleteMapping("/id/{id}")
	public ResponseEntity<String> deleteEntry( @PathVariable ObjectId id) {
		log.info("Deleting entry with id: {}", id);
		String username= UserUtill.getLoggedInUser();
		return new ResponseEntity<>(journalService.deleteEntry(username, id), HttpStatus.OK);
	}

}
