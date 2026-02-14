package com.example.App;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ROOT → INDEX
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("courses",
                jdbcTemplate.queryForList("SELECT course_name FROM courses"));

        return "register";
    }

    // ================= LOGIN LOGIC =================
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes ra) {

        Integer adminCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM admin WHERE username=? AND password=?",
                Integer.class, username.trim(), password.trim());

        if (adminCount != null && adminCount > 0) {
            session.setAttribute("username", username);
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin/dashboard";
        }

        Integer studentCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE username=? AND password=?",
                Integer.class, username.trim(), password.trim());

        if (studentCount != null && studentCount > 0) {
            session.setAttribute("username", username);
            session.setAttribute("role", "STUDENT");
            return "redirect:/student/dashboard";
        }

        ra.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }

    // ================= REGISTER LOGIC (FIX) =================
    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String course,
            @RequestParam(required = false) String otherCourse,
            @RequestParam String password,
            RedirectAttributes ra) {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE username=?",
                Integer.class, username.trim());

        if (count != null && count > 0) {
            ra.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }

        // if OTHER selected → use otherCourse value
        if ("OTHER".equals(course) && otherCourse != null && !otherCourse.isBlank()) {
            course = otherCourse.trim();
        }

        jdbcTemplate.update(
                "INSERT INTO students (name, username, email, course, password) VALUES (?, ?, ?, ?, ?)",
                name.trim(), username.trim(), email.trim(), course.trim(), password.trim());

        ra.addFlashAttribute("message", "Registration successful! Please login.");
        return "redirect:/login";
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
