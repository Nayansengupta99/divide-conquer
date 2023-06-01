package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "Users")
public class UserModel {
	
	
	
	@Transient
	public static final String SEQUENCE_NAME = "users_sequence";
	
	@Id
	
	private long userId;
	private String userName;
	private int userAge;
	
	
	
}
