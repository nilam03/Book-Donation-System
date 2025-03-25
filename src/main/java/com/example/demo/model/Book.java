package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String author;
	
	
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private BookStatus status = BookStatus.PENDING;
	
	 @ManyToOne
	  @JoinColumn(name = "user_id")
	   private User user;

	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus available) {
		this.status = available;
	}

	public Book(Long id, String title, String author, BookStatus status, User user) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.status = status;
		this.user = user;
	}

	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	
	
}