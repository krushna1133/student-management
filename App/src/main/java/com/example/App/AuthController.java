package com.example.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	   // ✅ ROOT URL FIX
    @GetMapping("/")
    public String home() {
        return "redirect:/run";
    }
	
    // ===============================
    // 1️⃣ LOGIN PAGE
    // ===============================
    @GetMapping("/run")
    public String loginPage() {
        return "login"; // your login HTML
    }

    // ===============================
    // 2️⃣ LOGIN SUBMIT (ROLE BASED)
    // ===============================
    @PostMapping("/submit")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        // ---------- ADMIN LOGIN ----------
        Integer adminCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username=? AND password=?",
            Integer.class, username, password
        );

        if (adminCount != null && adminCount > 0) {
            session.setAttribute("name", username);
            session.setAttribute("role", "ADMIN");
            return "redirect:/success"; // admin dashboard
        }

        // ---------- STUDENT LOGIN ----------
        Integer studentCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM students WHERE username=? AND password=?",
            Integer.class, username, password
        );

        if (studentCount != null && studentCount > 0) {
            session.setAttribute("name", username);
            session.setAttribute("role", "STUDENT");
            return "redirect:/student/dashboard"; // student dashboard
        }

        redirectAttributes.addFlashAttribute("error", "Invalid credentials");
        return "redirect:/run";
    }

    // ===============================
    // 3️⃣ ADMIN DASHBOARD
    // ===============================
    @GetMapping("/success")
    public String adminDashboard(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);

        Integer totalStudents = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM students", Integer.class
        );
        model.addAttribute("totalStudents", totalStudents);

        // course-wise stats (for pie chart)
        model.addAttribute("courseStats",
            jdbcTemplate.queryForList(
                "SELECT course, COUNT(*) AS count FROM students GROUP BY course"
            )
        );

        return "success"; // admin dashboard HTML
    }
	    
	 // ===============================
	 // ADMIN REGISTER PAGE
	 // ===============================
	 @GetMapping("/register")
	 public String registerPage() {
	     return "register";
	 }
	
	 // ===============================
	 // ADMIN REGISTER SUBMIT
	 // ===============================
	 @PostMapping("/register")
	 public String registerAdmin(@RequestParam String username,
	                             @RequestParam String password,
	                             @RequestParam String email,
	                             RedirectAttributes redirectAttributes) {
	
	     Integer count = jdbcTemplate.queryForObject(
	         "SELECT COUNT(*) FROM users WHERE username=?",
	         Integer.class, username
	     );
	
	     if (count != null && count > 0) {
	         redirectAttributes.addFlashAttribute(
	             "error", "Username already exists");
	         return "redirect:/register";
	     }
	
	     jdbcTemplate.update(
	         "INSERT INTO users(username,password,email,role) VALUES (?,?,?,?)",
	         username, password, email, "ADMIN"
	     );
	
	     redirectAttributes.addFlashAttribute(
	         "message", "Admin registered successfully. Please login.");
	
	     return "redirect:/run";
	 }
	
	 
 

    // ===============================
    // 4️⃣ LOGOUT
    // ===============================
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/run";
    }
}


