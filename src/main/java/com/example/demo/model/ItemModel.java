package com.example.demo.model;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemModel {
	
	
	@Transient
	public static final String SEQUENCE_NAME = "item_sequence";
	private long itemId;

	private String itemName;

	private double itemPrice;

	private Date purchaseDate;
}
