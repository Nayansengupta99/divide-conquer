package com.example.demo.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.time.Instant;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.model.DatabaseSequence;
import com.example.demo.model.ItemModel;
import com.example.demo.model.TripModel;
import com.example.demo.model.UserItemModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AppUserItemRepository;
import com.example.demo.repository.AppUserRepository;

@Service
public class ApplicationService {

	@Autowired
	private AppUserRepository userRepo;

	@Autowired
	private AppUserItemRepository userItemRepo;

	@Autowired
	private MongoOperations mongoOperations;

	public long generateSequence(String seqName) {

		DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), DatabaseSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1;

	}

	public List<UserModel> getAllUsers() {
		return userRepo.findAll();
	}

	public UserModel saveUser(UserModel model) {
		model.setUserId(generateSequence(UserModel.SEQUENCE_NAME));
		return userRepo.save(model);
	}

	public UserItemModel findUserByName(String userName) {
		return userItemRepo.findByUserName(userName);
	}

	
	
	public UserItemModel saveUserItems(UserItemModel model) {
		if (findUserByName(model.getUserName()) == null) {

			for (ItemModel m : model.getItems()) {
				Instant ins = Instant.now();
				m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
				m.setPurchaseDate(Date.from(ins));
			}
			
			model.getTripModel().setTripId(generateSequence(TripModel.SEQUENCE_NAME));
			userItemRepo.save(model);

			return model;
		}

		else {
			UserItemModel modelRef = findUserByName(model.getUserName());
			for (ItemModel m : model.getItems()) {
				modelRef.getItems().add(m);
			}

			return modelRef;
		}

	}

}
