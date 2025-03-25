package com.example.demo.controller;


import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;

import java.util.List;
@Controller
@RequestMapping("/books")
public class BookController {
   @Autowired
   private BookService bookService;
   // Show list of books
   
   @Autowired
   private BookRepository bookRepository;
   
   
   @GetMapping
   public String donatePage(Model model,HttpSession session,RedirectAttributes redirectAttributes) {
	   
	   //User loggedInUser=(User) session.getAttribute("loggedInUser");
	   Long userId=(Long) session.getAttribute("loggedInUserId"); 
	   String username=(String) session.getAttribute("username");
	     model.addAttribute("userId", userId);
	     
	     if (username == null) {
	          redirectAttributes.addFlashAttribute("errorMessage", "Please login first!");
	           return "redirect:/home"; // Redirect to user login page
	       }else
	       {
	      model.addAttribute("username",username);
	       return "book-list"; 
	       }// Returns the registration form page (Thymeleaf template)
	   }
   
   @GetMapping("/available")
   public String getAvailableBooks(Model model,HttpSession session,RedirectAttributes redirectAttributes) {
	   String username=(String) session.getAttribute("username");
	   User user=(User) session.getAttribute("loggedInUser");
	   Long userId=(Long) session.getAttribute("loggedInUserId");
	   model.addAttribute("userId", userId);
	   if (username == null) {
	          redirectAttributes.addFlashAttribute("errorMessage", "Please login first!");
	           return "redirect:/home"; // Redirect to user login page
	       }
	   
	   if(user!=null)
	   {
		   model.addAttribute("user", user);
	   }
       List<Book> books = bookService.getApprovedBooks();
       model.addAttribute("books", books); 
       model.addAttribute("username",username);// Send approved books to Thymeleaf
       return "available-books"; // Redirect to Thymeleaf page
   }
  
   // Show book form to add a new book
   @GetMapping("/new")
   public String showAddBookForm(Model model,HttpSession session,RedirectAttributes redirectAttributes) {
	   
	   String username=(String) session.getAttribute("username");
	   if (username == null) {
	          redirectAttributes.addFlashAttribute("errorMessage", "Please login first!");
	           return "redirect:/home"; // Redirect to user login page
	       }
       model.addAttribute("book", new Book());
       return "book-form"; // Returns book-form.html
   }
   // Add a new book (POST request)
   @PostMapping
   public String addBook(@ModelAttribute("book") Book book) {
       bookService.addBook(book);
       return "redirect:/books"; // Redirect to book list after saving
   }
  
//   // Update book
//   @PostMapping("/{id}")
//   public String updateBook(@PathVariable Long id, @ModelAttribute Book bookDetails) {
//       bookService.updateBook(id, bookDetails);
//       return "redirect:/books"; // Redirect to book list after updating
//   }
   
   // Delete book
   @GetMapping("/delete/{id}")
   public String deleteBook(@PathVariable Long id) {
       bookService.deleteBook(id);
       return "redirect:/books"; // Redirect to book list after deleting
   }
   // Borrow a book
   
   
   @GetMapping("/borrow/{userId}/{bookId}")
   public String borrowBook(@PathVariable Long userId, @PathVariable Long bookId,Model model,HttpSession session,RedirectAttributes redirectAttributes) {
	   
	   String username=(String) session.getAttribute("username");
	   if (username == null) {
	          redirectAttributes.addFlashAttribute("errorMessage", "Please login first!");
	           return "redirect:/home"; // Redirect to user login page
	       }
       bookService.borrowBook(userId, bookId);
       return "redirect:/books/borrowed/" + userId; // Redirect to user's borrowed books history
   }
   
   
   // Display Borrowed Books History Page
   @GetMapping("/borrowed/{userId}")
   public String showBorrowedBooks(@PathVariable Long userId, Model model,HttpSession session,RedirectAttributes redirectAttributes) {
	   String username=(String) session.getAttribute("username");
	   if (username == null) {
	          redirectAttributes.addFlashAttribute("errorMessage", "Please login first!");
	           return "redirect:/home"; // Redirect to user login page
	       }
       List<Book> borrowedBooks = bookService.getBorrowedBooksByUserId(userId);
       model.addAttribute("borrowedBooks", borrowedBooks);
       return "user-dashboard"; // Thymeleaf template for showing history
   }
   
   
  
   
   @GetMapping("/search")
	public String searchBooks(@RequestParam("title") String title, Model model) {
	   List<Book> books = bookService.searchAvailableBooks(title);
	   model.addAttribute("books", books);
	   return "available-books"; // Return the Thymeleaf template
	}
   
   @ExceptionHandler(BookNotFoundException.class)
   public String handleBookNotFoundException(BookNotFoundException ex, Model model) {
       model.addAttribute("error", ex.getMessage());
       return "available-books"; // Return the Thymeleaf page with the error message
   }
}