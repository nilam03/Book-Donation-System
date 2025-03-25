package com.example.demo.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
@Controller
@RequestMapping("/users")
public class UserController {

   @Autowired
   private UserService userService;
   @GetMapping("/login")
   public String loginpage(Model model,HttpSession sessions,RedirectAttributes redirectAttributes) {
	   
	   // retrieve user name from session
	   String username=(String) sessions.getAttribute("username");
	   
	   // add user name to model so it can be displayed on login page
       model.addAttribute("username",username);
       return "login"; // Returns the registration form page (Thymeleaf template)
   }
   // Show registration form
   @GetMapping("/register")
   public String showRegistrationForm(Model model,HttpSession sessions,RedirectAttributes redirectAttributes) {
	   
	   
       model.addAttribute("user", new User());
       return "register2"; // Returns the registration form page (Thymeleaf template)
   }
   
   // Show User history of donation and borrow
   @GetMapping("/userdashboard")
   public String showUserHistory(Model model) {
       model.addAttribute("user", new User());
       return "user-dashboard"; // Returns the user history page (Thymeleaf template)
   }

   
   @PostMapping("/login")
   public String loginUser (@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes,HttpSession session) {
      try {
    	  //prevents from multiple login
    	  if (session.getAttribute("loggedInUserId") != null || session.getAttribute("loggedInAdmin") != null) {
              redirectAttributes.addFlashAttribute("errorMessage", "Another user or admin is already logged in!");
              return "redirect:/users/login";
          }
    	
    	  // check user name and password is correct or not
    	  User user = userService.authenticate(username, password);
      
    	  
    	// if user is already exists by successful login then save user info in session
       if (user != null) {
       	session.setAttribute("username", user.getUsername());

       	session.setAttribute("loggedInUserId", user.getId());

       	session.setAttribute("loggedInUserRole", "USER");
       	
       	//it must be accessed after redirection
        redirectAttributes.addFlashAttribute("user", user);
           
           return "redirect:/books"; // Redirect to package list after successful login
      }
      
       // if authentication fails
       else
      {
    	  throw new RuntimeException("Invalid username or password.");
      }
      }
      
      // exception handing 
      catch (Exception ex)
      {
    	 
    	  System.out.println("login error :"+ex.getMessage());  
      redirectAttributes.addFlashAttribute("error", "Invalid username or password");
     //change 1
       return "redirect:/users/login"; // Return to login page with error
   }
      
   }
   
   
   @GetMapping("/logout")
   public String logout(HttpSession session) {
       session.invalidate(); // Destroy session on logout
       return "redirect:/home";
   }
   
   
   @PostMapping("/check-username")
   public String checkUsernameExists(@RequestParam String username, Model model) {
       boolean exists = userService.existsByUserName(username);
       model.addAttribute("usernameExists", exists);
       return "register2"; // Reloads the same page with the result
   }
   
   
   // Register a new user
   @PostMapping("/register")
   public String registerUser( @ModelAttribute("user") User user,Model model, RedirectAttributes redirectAttributes) {
	  	   
       if (userService.existsByUserName(user.getUsername())) {
    	   redirectAttributes.addFlashAttribute("error","User Already Exists");
           return "redirect:/users/register"; // Reloads the registration page with an error
       }
       else
       {
       userService.registerUser(user);
       model.addAttribute("success", "Registration successful!");
       return "redirect:/users/login";
       }// Redirects to the login page after success
   }
   
   
   
   
}
