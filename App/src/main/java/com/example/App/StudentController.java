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

    // ===============================
    // 1️⃣ ADMIN → STUDENT MANAGEMENT
    // ===============================
    @GetMapping("/students")
    public String students(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        model.addAttribute("name", session.getAttribute("name"));

        model.addAttribute("students",
            jdbcTemplate.queryForList("SELECT * FROM students ORDER BY id")
        );

        return "student"; // admin student management page
    }

    // ===============================
    // 2️⃣ ADD STUDENT (ADMIN)
    // ===============================
    @PostMapping("/student/add")
    public String addStudent(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String course,
                             @RequestParam String username,
                             @RequestParam String password,
                             HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update(
            "INSERT INTO students(name,email,course,username,password,role) VALUES (?,?,?,?,?,?)",
            name, email, course, username, password, "STUDENT"
        );

        return "redirect:/students";
    }


    // ===============================
    // 3️⃣ UPDATE STUDENT (ADMIN)
    // ===============================
    @PostMapping("/student/update")
    public String updateStudent(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String course,
                                HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update(
            "UPDATE students SET name=?, email=?, course=? WHERE id=?",
            name, email, course, id
        );

        return "redirect:/students";
    }

    // ===============================
    // 4️⃣ DELETE STUDENT (ADMIN)
    // ===============================
    @PostMapping("/student/delete")
    public String deleteStudent(@RequestParam Long id, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update("DELETE FROM students WHERE id=?", id);
        return "redirect:/students";
    }

    // ===============================
    // 5️⃣ STUDENT DASHBOARD
    // ===============================
    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model, HttpSession session) {

        if (!"STUDENT".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        String username = (String) session.getAttribute("name");

        model.addAttribute("student",
            jdbcTemplate.queryForMap(
                "SELECT * FROM students WHERE username=?", username
            )
        );

        return "student-dashboard";
    }
    
    
    @PostMapping("/student/update-profile")
    public String updateStudentProfile(@RequestParam String username,
                                       @RequestParam String email,
                                       @RequestParam String password,
                                       HttpSession session) {

        if (!"STUDENT".equals(session.getAttribute("role"))) {
            return "redirect:/run";
        }

        jdbcTemplate.update(
            "UPDATE students SET email=?, password=? WHERE username=?",
            email, password, username
        );

        return "redirect:/student/dashboard";
    }

}
