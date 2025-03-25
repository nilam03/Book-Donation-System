package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Book;
import com.example.demo.model.BookStatus;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	List<Book> findByStatus(BookStatus status);
	
	List<Book> findByTitleContainingIgnoreCaseAndStatus(String title,BookStatus status);
	
	List<Book> findByUserIdAndStatus(Long userId,BookStatus borrowed);
	
	List<Book> findByStatusIn(List<BookStatus> statuses);
	
}
