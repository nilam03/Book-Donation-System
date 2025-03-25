package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
//	hard coded admin credentials
	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "admin123";
	
	public boolean authenticate (String username,String password)
	{
		return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
	}

}
