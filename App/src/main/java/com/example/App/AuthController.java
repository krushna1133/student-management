package com.example.App;

import jakarta.servlet.http.HttpSession;
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

    // REGISTER PAGE
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // LOGIN LOGIC
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

    // LOGOUT
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
