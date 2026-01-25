package com.example.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/students")
    public String students(Model model, HttpSession session) {

        if (session.getAttribute("name") == null) {
            return "redirect:/run";
        }

        String username = (String) session.getAttribute("name");
        model.addAttribute("name", username);

        model.addAttribute(
            "students",
            jdbcTemplate.queryForList("SELECT * FROM students ORDER BY id")
        );

        return "student";
    }


    @PostMapping("/student/add")
    public String addStudent(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String course) {

        jdbcTemplate.update(
            "INSERT INTO students(name,email,course) VALUES (?,?,?)",
            name, email, course
        );
        return "redirect:/students";
    }

    @PostMapping("/student/update")
    public String updateStudent(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String course) {

        jdbcTemplate.update(
            "UPDATE students SET name=?, email=?, course=? WHERE id=?",
            name, email, course, id
        );
        return "redirect:/students";
    }

    @PostMapping("/student/delete")
    public String deleteStudent(@RequestParam Long id) {

        jdbcTemplate.update("DELETE FROM students WHERE id=?", id);
        return "redirect:/students";
    }
}
