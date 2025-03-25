package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
   @GetMapping("/home")
   public String showLandingPage(Model model) {
       model.addAttribute("title", "Welcome to Book Donation Campaign");
       return "index"; // Renders index.html from src/main/resources/templates
   }
}