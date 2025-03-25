package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Book;
import com.example.demo.model.BookStatus;
import com.example.demo.model.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private BookService bookService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String showAdminLoginPage() {
		return "admin-login";
	}

	@PostMapping("/login")
	public String adminLogin(@RequestParam String username, @RequestParam String password, HttpSession session,
			RedirectAttributes redirectAttributes) {

		// Check if a user is logged in

		if (session.getAttribute("loggedInUserId") != null) {

			redirectAttributes.addFlashAttribute("errorMessage",
					"A user is already logged in. Admin login is not allowed.");

			return "redirect:/admin/login";

		}

		// Hardcoded admin credentials

		if (adminService.authenticate(username, password)) {
			session.setAttribute("loggedInAdmin", username);
			session.setAttribute("loggedInUserRole", "ADMIN");

			return "redirect:/admin"; // Redirect to admin dashboard

		}

		redirectAttributes.addFlashAttribute("errorMessage", "Invalid admin credentials.");

		return "redirect:/admin/login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		return "redirect:/home";
	}

//	admin dashboard : Manage Users&Books

	@GetMapping
	public String adminDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

	String username=(String) session.getAttribute("username");

	if (username != null) {

	redirectAttributes.addFlashAttribute("errorMessage", "Please login first!");

	return "redirect:/admin/login"; // Redirect to user login page

	}

	List<User> users = userService.getAllUsers();

	List<Book> books = bookService.getBooksForAdmin();

	model.addAttribute("users", users);

	model.addAttribute("books", books);

	return "admin-dashboard"; // Returns Thymeleaf template admin-dashboard.html
	}
	
	// Manage Users Delete User

	@GetMapping("/users/delete/{id}")

	public String deleteUser(@PathVariable Long id) {

		userService.deleteUser(id); // Call dlete method in UserService

		return "redirect:/admin"; // Redirect back to the Admin Dashboard

	}

	// Approve or Reject Book

	@GetMapping("/books/approve/{id}")

	public String approveBook (@PathVariable Long id) {

	Book book = bookService.getBookById(id);

	if (book != null) {

	book.setStatus(BookStatus.APPROVED);

	bookService.saveBook (book); // Update book status to "APPROVED"

	}

	return "redirect:/admin"; // Redirect back to the Admin Dashboard

	}

	@GetMapping("/books/reject/{id}")

	public String rejectBook (@PathVariable Long id) {

	Book book = bookService.getBookById(id);

	if (book != null) {

	book.setStatus (BookStatus.REJECTED);

	bookService.saveBook(book); // Update book status to "REJECTED"

	}

	return "redirect:/admin"; // Redirect back to the Admin Dashboard
	}

}
