package com.example.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.BookStatus;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
public class BookService {
   @Autowired
   private BookRepository bookRepository;
   
   @Autowired
   private UserRepository userRepository;

   
   public List<Book> getAllBooks() {
       return bookRepository.findAll();
   }
   
   
   public List<Book> getBooksForAdmin() {
       List<BookStatus> statuses = Arrays.asList(BookStatus.PENDING, BookStatus.APPROVED, BookStatus.REJECTED);
       return bookRepository.findByStatusIn(statuses);
   }
   
   
   public void addBook(Book book) {
       bookRepository.save(book);
   }
   public void updateBook(Long id, Book bookDetails) {
       Book book = bookRepository.findById(id).orElse(null);
       if (book != null) {
           book.setTitle(bookDetails.getTitle());
           book.setAuthor(bookDetails.getAuthor());
           book.setStatus(bookDetails.getStatus());
           bookRepository.save(book);
       }
   }
   
   
   public void deleteBook(Long id) {
       bookRepository.deleteById(id);
   }
   
   
   @Transactional
   public boolean borrowBooks(Long bookId, Long userId) {
       Optional<Book> optionalBook = bookRepository.findById(bookId);
       if (optionalBook.isPresent()) {
           Book book = optionalBook.get();
           if (book.getStatus() == BookStatus.AVAILABLE) { // Check if book is available
               book.setStatus(BookStatus.BORROWED);
               bookRepository.save(book);
              
               return true;
           }
       }
       return false;
   }

   public void borrowBook(Long userId, Long bookId) {
       Optional<Book> bookOptional = bookRepository.findById(bookId);
       Optional<User> userOptional = userRepository.findById(userId);
       if (bookOptional.isPresent() && userOptional.isPresent()) {
           Book book = bookOptional.get();
           User user = userOptional.get();
           book.setStatus(BookStatus.BORROWED); // Update book status
           book.setUser(user); // Assign book to user
           bookRepository.save(book);
       }
   }
   
   public List<Book> getBorrowedBooksByUserId(Long userId) {
       return bookRepository.findByUserIdAndStatus(userId, BookStatus.BORROWED);
   }
   
   
   
   
   public Book getBookById(Long id) {
       Optional<Book> book = bookRepository.findById(id);
       return book.orElse(null); // Return the book if found, otherwise null
   }

   public List<Book> searchAvailableBooks(String title) {
		List<Book> books = bookRepository.findByTitleContainingIgnoreCaseAndStatus(title,BookStatus.APPROVED);
	    if (books.isEmpty()) {
	        throw new BookNotFoundException("No books found with title: " + title);
	    }
	    return books;
	}

public Book saveBook(Book book) {
	// TODO Auto-generated method stub
	return bookRepository.save(book);
}
public List<Book> getApprovedBooks() {
    return bookRepository.findByStatus(BookStatus.APPROVED); // Fetch only approved books
}



}