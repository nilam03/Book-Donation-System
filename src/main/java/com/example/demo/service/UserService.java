package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public void save(User user)
	{
		userRepository.save(user);
	}
	
	public void deleteUser(Long id)
	{
		userRepository.deleteById(id);
	}
	
	public List<User> getAllUsers()
	{
		return userRepository.findAll();
	}
	
	public User getUserById(Long id)
	{
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}
	
	public boolean existsByUserName(String username)
	{
		return userRepository.existsByUsername(username);
	}
	
	public User registerUser(User user)
	{
		return userRepository.save(user);
	}
	
	public boolean isEmailTaken(String email)
	{
		return userRepository.findByEmail(email).isPresent();
	}
	
	public User authenticate(String username,String password)
	{
		User user = userRepository.findByUsername(username);
		
		if(user == null || user.getPassword()==null || !user.getPassword().equals(password))
		{
			throw new RuntimeException("Invalid");
		}
		return user;
	}

}
