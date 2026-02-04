package com.example.App;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // DASHBOARD
    @GetMapping("/student/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (!"STUDENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        String username = (String) session.getAttribute("username");

        model.addAttribute("name", username);
        model.addAttribute("activePage", "dashboard");

        model.addAttribute("student",
                jdbcTemplate.queryForMap(
                        "SELECT * FROM students WHERE username=?", username));

        return "student-dashboard";
    }

    // UPDATE PROFILE PAGE
    @GetMapping("/student/profile")
    public String profile(HttpSession session, Model model) {

        if (!"STUDENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        String username = (String) session.getAttribute("username");

        model.addAttribute("name", username);
        model.addAttribute("activePage", "profile");

        model.addAttribute("student",
                jdbcTemplate.queryForMap(
                        "SELECT * FROM students WHERE username=?", username));

        return "student-updateprofile";
    }

    // UPDATE PROFILE SUBMIT
    @PostMapping("/student/update-profile")
    public String updateProfile(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes ra) {

        String username = (String) session.getAttribute("username");

        jdbcTemplate.update(
                "UPDATE students SET email=?, password=? WHERE username=?",
                email, password, username);

        ra.addFlashAttribute("message", "Profile updated successfully");
        return "redirect:/student/profile";
    }
}
