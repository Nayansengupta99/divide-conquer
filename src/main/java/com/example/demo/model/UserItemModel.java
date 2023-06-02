package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "UserItem")
public class UserItemModel {
	
	@Transient
	public static final String SEQUENCE_NAME = "item_sequence";
	
	@Id
	private long id;
	private String userId;
	private String userName;
	private TripModel tripModel;
	private List<ItemModel> items;


}
