package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.example.demo.model.UserModel;

public interface AppUserRepository extends MongoRepository<UserModel, Integer>{

	UserModel findByUserName(String userName);
	
}
