package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins ="https://divide-conquer-frontend-ybb9.vercel.app/",maxAge = 3600)
public class ApplicationController {

	@Autowired
	private ApplicationService appService;

	@GetMapping("/users")
	public List<UserModel> getAllUsers() {
		return appService.getAllUsers();
	}



	@PostMapping("/saveuseritems")
	public UserItemModel saveUserItems(@RequestBody UserItemModel model) {
		return appService.saveUserItems(model);
	}

	@PostMapping("/saveuser")
	public String saveUser(@RequestBody UserModel model) {
		return appService.saveUser(model);
	}
	
	@GetMapping("/totalexpenditure/{tripName}")
	public Map<String,Double> getEveryOneTotalExpenditure(@PathVariable String tripName){
		return appService.getEveryOneTotalExpenditure(tripName);
	}
	
	@GetMapping("/share/{tripName}")
	public Map<String,Double> getEveryOneShare(@PathVariable String tripName){
		return appService.getEachUserShare(tripName);
	}
	
	@GetMapping("/getuseritem/{userName}")
	public UserItemModel findUserItemModelByUserName(@PathVariable String userName) {
		return appService.findUserItemByUserName(userName);
	}
	
	@GetMapping("/trips")
	public List<String> getAllTripModels(){
		return appService.getAllTripModels();
	}
	
	
	
	

}
