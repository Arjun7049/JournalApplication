package com.journal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.journal.exception.JournalException;
import com.journal.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.repository.JournalRepository;
import com.journal.repository.UserRepository;

@Service
@Slf4j
@Transactional
public class JournalService {
	
	@Autowired
	private JournalRepository journalRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public JournalEntry saveEntry(String username, JournalEntry journalEntry) {
		try{
			User user= userRepository.findByUsername(username);
			journalEntry.setTimestamp(LocalDateTime.now());
			JournalEntry savedEntry= journalRepository.save(journalEntry);
			user.getJournalEntries().add(savedEntry);
			userRepository.save(user);
			return savedEntry;
		}catch (Exception e){
			log.error("Error saving journal entry: {}", e.getMessage());
			throw new RuntimeException("Error saving journal entry: " + e.getMessage());
		}
	}
	public JournalEntry saveEntry(JournalEntry journalEntry) {
		 JournalEntry savedEntry= journalRepository.save(journalEntry);
		return savedEntry;
	}
	public List<JournalEntry> getAllEntriesOfUser(String username) throws UserException {
		User user = userRepository.findByUsername(username);
		try{
			if(user.getJournalEntries().size()==0){
				throw new UserException("User has no journal entries yet. Please add an entry first.");
			}
		}catch (UserException ex){
			log.error("User has no journal entries: {}", ex);
			throw ex;
		}
		return user.getJournalEntries();
	}
	public JournalEntry getEntry(ObjectId id) {	
		return journalRepository.findById(id).orElse(null);
	}
	public JournalEntry getUserEntry(String username, ObjectId id){
		try{
			User user = userRepository.findByUsername(username);
			return user.getJournalEntries().stream()
					.filter(entry -> entry.getId().equals(id))
					.findFirst()
					.orElseThrow(() -> new JournalException("Entry not found for user: " + username + " with ID: " + id ));
		}catch (JournalException e){
			log.error("Error retrieving journal entry for user {}: {}", username, e);
			throw new JournalException("Entry not found for user: " + username + " with ID: " + id );
		}
	}
	public String deleteEntry(String username, ObjectId id) {
		User user = userRepository.findByUsername(username);
		List<JournalEntry> collectedEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
				.collect(Collectors.toList());
		try{
			if (collectedEntries!=null && !collectedEntries.isEmpty() &&collectedEntries.size() != 0){
				JournalEntry entryToDelete = collectedEntries.get(0);
				log.info("Deleting journal entry with id: {} for user: {}", id, username);
				user.getJournalEntries().removeIf(x->x.getId().equals(id));
				userRepository.save(user);
				journalRepository.deleteById(id);
				return "Entry with id: " + id + " deleted successfully for user: " + username;
			}
			throw new JournalException("No entry found with id: " + id + " for user: " + username);
		}catch (JournalException e){
			log.error("Error deleting journal entry: {}", id);
			throw new JournalException("No entry found with id: " + id + " for user: " + username);
		} catch (Exception e) {
			log.error("Error deleting journal entry: {}", e.getMessage());
			throw new RuntimeException("Error deleting journal entry: " + e.getMessage());
		}
	}
	public JournalEntry updateJournalEntry(String username, ObjectId id, JournalEntry journalEntry){
		try{
			User user = userRepository.findByUsername(username);
			List<JournalEntry> collectedEntries = user.getJournalEntries().stream()
					.filter(x -> x.getId().equals(id))
					.toList();

			if (collectedEntries != null && !collectedEntries.isEmpty()) {
				JournalEntry oldEntry = collectedEntries.get(0);

				oldEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().strip().isEmpty()
						? journalEntry.getContent()
						: oldEntry.getContent());
				oldEntry.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().strip().isEmpty()
						? journalEntry.getTitle()
						: oldEntry.getTitle());
				return saveEntry(oldEntry);
			}
			throw new JournalException("No entry found with id: " + id);
		}catch (JournalException ex){
			log.error("Error updating journal entry: {}", ex);
			throw ex;
		}

	}

}
