package com.journal.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Document(collection ="jounal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {
	
	@Id
	private ObjectId id;
	private LocalDateTime timestamp;
	@NonNull
	private String title;
	private String content;

}
