package com.example.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class CourseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ===============================
    // COURSES PAGE (ADMIN ONLY)
    // ===============================
    @GetMapping("/courses")
    public String courses(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        model.addAttribute("name", session.getAttribute("name"));

        model.addAttribute("courses",
            jdbcTemplate.queryForList("SELECT * FROM courses ORDER BY id")
        );

        return "courses";
    }

    // ===============================
    // ADD COURSE
    // ===============================
    @PostMapping("/course/add")
    public String addCourse(@RequestParam String name,
                            @RequestParam String description,
                            HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update(
            "INSERT INTO courses(name, description) VALUES (?, ?)",
            name, description
        );

        return "redirect:/courses";
    }

    // ===============================
    // UPDATE COURSE
    // ===============================
    @PostMapping("/course/update")
    public String updateCourse(@RequestParam Long id,
                               @RequestParam String name,
                               @RequestParam String description,
                               HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update(
            "UPDATE courses SET name=?, description=? WHERE id=?",
            name, description, id
        );

        return "redirect:/courses";
    }

    // ===============================
    // DELETE COURSE
    // ===============================
    @PostMapping("/course/delete")
    public String deleteCourse(@RequestParam Long id,
                               HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update(
            "DELETE FROM courses WHERE id=?", id
        );

        return "redirect:/courses";
    }
}
