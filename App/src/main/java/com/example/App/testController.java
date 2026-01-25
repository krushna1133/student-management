package com.example.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class testController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Login page
    @GetMapping("/run")
    public String run() {
        return "NewFile";
    }

    // Register page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Register user
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               RedirectAttributes redirectAttributes) {

        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, username);

        if (count != null && count > 0) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }

        String insertSql =
            "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, username, password, email);

        redirectAttributes.addFlashAttribute(
            "message", "Registration successful! Please login.");
        return "redirect:/run";
    }

    // Login
    @PostMapping("/submit")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        String sql =
            "SELECT COUNT(*) FROM users WHERE username=? AND password=?";
        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, username, password);

        if (count != null && count > 0) {
            session.setAttribute("name", username); // ✅ STORE USER
            return "redirect:/success";
        } else {
            redirectAttributes.addFlashAttribute(
                "error", "Invalid username or password");
            return "redirect:/run";
        }
    }

    @GetMapping("/success")
    public String success(Model model, HttpSession session) {

        if (session.getAttribute("name") == null) {
            return "redirect:/run";
        }

        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);

        // Total students
        Integer totalStudents = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM students", Integer.class);
        model.addAttribute("totalStudents", totalStudents);

        // ✅ Course-wise students count
        model.addAttribute("courseStats",
            jdbcTemplate.queryForList(
                "SELECT course, COUNT(*) AS count FROM students GROUP BY course"
            )
        );

        return "success";
    }


    // Update profile
    @PostMapping("/update")
    public String updateUser(@RequestParam String oldUsername,
                             @RequestParam String newUsername,
                             @RequestParam String newPassword,
                             @RequestParam String newEmail,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        String sql =
            "UPDATE users SET username=?, password=?, email=? WHERE username=?";

        int rows = jdbcTemplate.update(
                sql,
                newUsername,
                newPassword,
                newEmail,
                oldUsername
        );

        if (rows > 0) {
            session.setAttribute("name", newUsername); // ✅ UPDATE SESSION
            redirectAttributes.addFlashAttribute("name", newUsername);
        } else {
            redirectAttributes.addFlashAttribute("error", "Update failed");
        }

        return "redirect:/success";
    }

    // Logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/run";
    }
    
    @GetMapping("/student/forms")
    public String studentForms() {
        return "student-form";
    }

}
