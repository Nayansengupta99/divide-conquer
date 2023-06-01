package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.UserItemModel;

public interface AppUserItemRepository extends MongoRepository<UserItemModel, Integer>{

UserItemModel findByUserName(String userName);




}
