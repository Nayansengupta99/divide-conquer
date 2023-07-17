package com.example.demo.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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
import com.example.demo.util.Util;

import lombok.extern.slf4j.Slf4j;

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

	public boolean isUserAlreadyRegistered(String userName) {
		// boolean flag=false;
		List<UserModel> userList = userRepo.findAll();
		HashMap<String, String> userMap = new HashMap<String, String>();
		for (UserModel m : userList) {
			userMap.put(m.getUserId(), m.getUserName());
		}

		if (userMap.containsValue(userName)) {
			return true;
		} else {
			return false;
		}
	}

	public String saveUser(UserModel model) {

		if (model.getUserName() != null && model.getUserAge() != 0) {
			if (isUserAlreadyRegistered(model.getUserName())==true) {

				return model.getUserName() + " is already registered";
			} else {
				userRepo.save(model);

				return "Details are successfully saved for " + model.getUserName();
			}
		} else if(model.getUserName() == null || model.getUserAge() == 0) {
			return "Please fill the form properly";
		}
		else if(model.getUserName() != null || model.getUserAge() == 0) {
			return "Please fill the form properly";
		}
		else {
			return "Please fill the form properly";
		}
	}

	public UserModel findUserByName(String userName) {
		return userRepo.findByUserName(userName);
	}

	public UserItemModel findUserItemByUserName(String userName) {
		return userItemRepo.findByUserName(userName);
	}

	public List<String> getAllTripModels() {
		List<UserItemModel> userItemList = getAllUserAlongWithItems();
		Set<String> tripModelNames = new HashSet<String>();

		for (UserItemModel m : userItemList) {
			tripModelNames.add(m.getTripModel().getTripName());
		}
		List<String> tripNames = new ArrayList<>(tripModelNames);
		return tripNames;
	}

	public boolean isUserNameHavingUserItem(String userName) {
		boolean flag = false;
		List<UserItemModel> userItemList = getAllUserAlongWithItems();
		for (UserItemModel m : userItemList) {
			if (m.getUserName().equals(userName)) {
				flag = true;
				break;
			}
		}
		return flag;
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

	public UserItemModel getUserItemByUserNameAndTripName(String userName, String tripName) {

		UserItemModel model = null;
		List<UserItemModel> userItemList = getAllUserAlongWithItems();

		for (UserItemModel m : userItemList) {
			if (m.getUserName().equals(userName) && m.getTripModel().getTripName().equals(tripName)) {
				return m;
			}
		}

		return model;
	}

	public UserItemModel saveUserItems(UserItemModel model) {

		if (isUserNameHavingUserItem(model.getUserName()) == false
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

		else if (isUserNameHavingUserItem(model.getUserName()) == false
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

		else if (isUserNameHavingUserItem(model.getUserName()) != false
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
			UserItemModel modelRef = getUserItemByUserNameAndTripName(model.getUserName(),
					model.getTripModel().getTripName());
			if (modelRef != null) {
				for (ItemModel m : model.getItems()) {
					Instant ins = Instant.now();
					modelRef.getItems().add(m);
					m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
					m.setPurchaseDate(Date.from(ins));
				}

				userItemRepo.save(modelRef);

				return modelRef;
			}

			else {

				for (ItemModel m : model.getItems()) {
					Instant ins = Instant.now();
					m.setItemId(generateSequence(ItemModel.SEQUENCE_NAME));
					m.setPurchaseDate(Date.from(ins));
				}
				model.setId(generateSequence(UserItemModel.SEQUENCE_NAME));
				model.setUserId(findUserByName(model.getUserName()).getUserId());
				model.getTripModel()
						.setTripId(getTripDetailsForTripName(model.getTripModel().getTripName()).getTripId());
				userItemRepo.save(model);

				return model;

			}
		}

	}

	public double getTotalExpenditureforUserName(String tripName, String userName) {

		double totalSum = 0.0f;

		UserItemModel model = new UserItemModel();

		for (UserItemModel m : getAllUserAlongWithItems()) {

			if (m.getTripModel().getTripName().toLowerCase().equals(tripName.toLowerCase())
					&& m.getUserName().equalsIgnoreCase(userName)) {
				model = m;
			}
		}

		for (ItemModel m : model.getItems()) {

			totalSum += m.getItemPrice();
		}
		return totalSum;
	}

	public Map<String, Double> getEveryOneTotalExpenditure(String tripName) {

		List<UserItemModel> userItemList = getAllUserAlongWithItems();

		TreeMap<String, Double> userPriceMap = new TreeMap<String, Double>();

		for (UserItemModel m : userItemList) {
			if (m.getTripModel().getTripName().equalsIgnoreCase(tripName)) {
				userPriceMap.put(m.getUserName(), getTotalExpenditureforUserName(tripName, m.getUserName()));
			}
		}

		Util util = new Util();

		Map<String, Double> sorteduserPriceMap = util.sortByValues(userPriceMap);
		Map<String, Double> reverseSorteduserPriceMap = new TreeMap<>(Collections.reverseOrder());
		reverseSorteduserPriceMap.putAll(sorteduserPriceMap);

		return sorteduserPriceMap;

	}

	public Map<String, Double> getEachUserShare(String tripName) {

		double totalExpenditure = 0.0;
		Map<String, Double> userItemMap = getEveryOneTotalExpenditure(tripName);

		for (Map.Entry<String, Double> map : userItemMap.entrySet()) {
			totalExpenditure += map.getValue();
		}

		Map<String, Double> eachShareMap = new HashMap<String, Double>();
		for (Map.Entry<String, Double> map : userItemMap.entrySet()) {
			if (totalExpenditure / userItemMap.size() > map.getValue()) {
				eachShareMap.put(map.getKey() + " should give extra",
						totalExpenditure / userItemMap.size() - map.getValue());
			} else if (totalExpenditure / userItemMap.size() < map.getValue()) {
				eachShareMap.put(map.getKey() + " should receive money",
						map.getValue() - totalExpenditure / userItemMap.size());
			} else {
				eachShareMap.put(map.getKey() + " should give extra", totalExpenditure / userItemMap.size());
			}
		}

		return eachShareMap;
	}

}
