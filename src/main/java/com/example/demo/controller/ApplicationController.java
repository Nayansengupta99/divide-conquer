package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserItemModel;
import com.example.demo.model.UserModel;
import com.example.demo.service.ApplicationService;

@RestController
@RequestMapping("divcon")
public class ApplicationController {

	@Autowired
	private ApplicationService appService;

	@GetMapping("/items")
	public List<UserModel> getAllUsers() {
		return appService.getAllUsers();
	}

	@GetMapping("/{userName}")
	public UserItemModel findUserItemByName(@PathVariable String userName) {
		return appService.findUserItemByUserName(userName);
	}

	@PostMapping("/saveuseritems")
	public UserItemModel saveUserItems(@RequestBody UserItemModel model) {
		return appService.saveUserItems(model);
	}

	@PostMapping("/saveuser")
	public UserModel saveUser(@RequestBody UserModel model) {
		return appService.saveUser(model);
	}

}
