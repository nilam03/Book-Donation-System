package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

// use validation 
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Username is required")
	@Column(nullable = false, unique = true)
	private String username;
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Column(nullable = false, unique = true)
	private String email;
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	@Column(nullable = false)
	private String password;
	private String role = "USER";
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	   private List<Book> borrowedBooks;  // One user can borrow multiple books
	   // Getters and Setters
	   public Long getId() {
	       return id;
	   }
	   public void setId(Long id) {
	this.id = id;
	   }
	   public String getUsername() {
	       return username;
	   }
	   public void setUsername(String username) {
	       this.username = username;
	   }
	   public String getEmail() {
	       return email;
	   }
	   public void setEmail(String email) {
	       this.email = email;
	   }
	   public String getPassword() {
	       return password;
	   }
	   public void setPassword(String password) {
	       this.password = password;
	   }
	   public List<Book> getBorrowedBooks() {
	       return borrowedBooks;
	   }
	   public void setBorrowedBooks(List<Book> borrowedBooks) {
	       this.borrowedBooks = borrowedBooks;
	   }
	}

	

