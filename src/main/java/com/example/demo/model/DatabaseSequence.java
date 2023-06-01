package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.Data;

@Document(collection = "database_sequences_dc")
@Data
public class DatabaseSequence {
	@Id
	private String id;

	private long seq;
}

