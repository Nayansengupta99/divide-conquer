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

	public List<UserItemModel> getAllUserAlongWithItems() {
		return userItemRepo.findAll();
	}

	public UserModel saveUser(UserModel model) {
		return userRepo.save(model);
	}

	public UserModel findUserByName(String userName) {
		return userRepo.findByUserName(userName);
	}

	public UserItemModel findUserItemByUserName(String userName) {
		return userItemRepo.findByUserName(userName);
	}

	public TripModel getTripDetailsForTripName(String tripName) {
		TripModel model = null;
		List<UserItemModel> userItemList = getAllUserAlongWithItems();

		for (UserItemModel m : userItemList) {
			if (m.getTripModel().getTripName().equals(tripName)) {
				return m.getTripModel();
			}
		}

		return model;

	}

	public UserItemModel saveUserItems(UserItemModel model) {

		if (findUserItemByUserName(model.getUserName()) == null
				&& getTripDetailsForTripName(model.getTripModel().getTripName()) == null) {

			for (ItemModel m : model.getItems()) {
				Instant ins = Instant.now();
				m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
				m.setPurchaseDate(Date.from(ins));
			}
			model.setId(generateSequence(UserItemModel.SEQUENCE_NAME));
			model.setUserId(findUserByName(model.getUserName()).getUserId());
			model.getTripModel().setTripId(generateSequence(TripModel.SEQUENCE_NAME));
			userItemRepo.save(model);

			return model;
		}

		else if (findUserItemByUserName(model.getUserName()) == null
				&& getTripDetailsForTripName(model.getTripModel().getTripName()) != null) {

			for (ItemModel m : model.getItems()) {
				Instant ins = Instant.now();
				m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
				m.setPurchaseDate(Date.from(ins));
			}
			model.setId(generateSequence(UserItemModel.SEQUENCE_NAME));
			model.setUserId(findUserByName(model.getUserName()).getUserId());
			model.getTripModel().setTripId(getTripDetailsForTripName(model.getTripModel().getTripName()).getTripId());
			userItemRepo.save(model);

			return model;

		}

		else if (findUserItemByUserName(model.getUserName()) != null
				&& getTripDetailsForTripName(model.getTripModel().getTripName()) == null) {

			for (ItemModel m : model.getItems()) {
				Instant ins = Instant.now();
				m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
				m.setPurchaseDate(Date.from(ins));
			}

			model.setId(generateSequence(UserItemModel.SEQUENCE_NAME));
			model.setUserId(findUserByName(model.getUserName()).getUserId());
			model.getTripModel().setTripId(generateSequence(TripModel.SEQUENCE_NAME));
			userItemRepo.save(model);

			return model;

		}

		else {
			UserItemModel modelRef = findUserItemByUserName(model.getUserName());

			for (ItemModel m : model.getItems()) {
				Instant ins = Instant.now();
				modelRef.getItems().add(m);
				m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
				m.setPurchaseDate(Date.from(ins));
			}

			userItemRepo.save(modelRef);
			return modelRef;
		}

	}

}
