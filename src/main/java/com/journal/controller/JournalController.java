package com.journal.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.journal.UserUtill;
import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.service.JournalService;
import com.journal.service.UserService;

@RestController
@RequestMapping("/journal")
public class JournalController {

	@Autowired
	private JournalService journalService;

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<JournalEntry> saveEntry(@RequestBody JournalEntry journalEntry) {

		try {
			String username = UserUtill.getLoggedInUser();
			return new ResponseEntity<>(journalService.saveEntry(username, journalEntry), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping
	public ResponseEntity<?> getAllEntriesOfUser() {
		try {
			String username = UserUtill.getLoggedInUser();
			return new ResponseEntity<List<JournalEntry>>(journalService.getAllEntriesOfUser(username), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/id/{id}")
	public ResponseEntity<?> getEntry(@PathVariable ObjectId id) {

		try {
			String username = UserUtill.getLoggedInUser();
			User user = userService.getUserByUsername(username);
			List<JournalEntry> collectedId = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
					.collect(Collectors.toList());
			if (collectedId != null && !collectedId.isEmpty()) {
				return new ResponseEntity<>(collectedId, HttpStatus.OK);
			}

			return new ResponseEntity<>("No entry found!!", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/id/{id}")
	public ResponseEntity<?> updateEntity(@PathVariable ObjectId id,
			@RequestBody JournalEntry journalEntry) {
		try {
			String username= UserUtill.getLoggedInUser();
			User user = userService.getUserByUsername(username);
			List<JournalEntry> collectedEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
					.collect(Collectors.toList());

			if (collectedEntries != null && !collectedEntries.isEmpty()) {
				JournalEntry oldEntry=collectedEntries.get(0);
				
				oldEntry.setContent(journalEntry.getContent() != null && journalEntry.getContent().strip() != ""
						? journalEntry.getContent()
						: oldEntry.getContent());
				oldEntry.setTitle(journalEntry.getTitle() != null && journalEntry.getTitle().strip() != ""
						? journalEntry.getTitle()
						: oldEntry.getTitle());
				return new ResponseEntity<>(journalService.saveEntry(oldEntry), HttpStatus.OK);
			}
			return new ResponseEntity<>("No entry found with id: "+id, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/id/{id}")
	public ResponseEntity<String> deleteEntry( @PathVariable ObjectId id) {

		try {
			String username= UserUtill.getLoggedInUser();
			User user = userService.getUserByUsername(username);
			List<JournalEntry> collectedEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
					.collect(Collectors.toList());

			if (collectedEntries != null && !collectedEntries.isEmpty()) {
				JournalEntry oldEntry=collectedEntries.get(0);
				return new ResponseEntity<>(journalService.deleteEntry(username, oldEntry.getId()), HttpStatus.OK);
			}
				
			return new ResponseEntity<>("No entry found!!", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
