package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.AppItemRepository;
import com.example.demo.repository.AppUserRepository;

@Service
public class ApplicationService {

	
	@Autowired
	private AppItemRepository itemRepo;
	
	@Autowired
	private AppUserRepository userRepo;
	
	
	
	
	
	
}
