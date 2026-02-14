package com.example.App;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  // ================= DASHBOARD =================
  @GetMapping("/admin/dashboard")
  public String dashboard(HttpSession session, Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username")); // REQUIRED
    model.addAttribute("activePage", "dashboard");

    Integer total = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM students", Integer.class);
    model.addAttribute("totalStudents", total);

    var rows = jdbcTemplate.queryForList(
        "SELECT course, COUNT(*) AS count FROM students GROUP BY course");

    List<String> labels = new ArrayList<>();
    List<Integer> counts = new ArrayList<>();

    for (var row : rows) {
      labels.add(row.get("course").toString());
      counts.add(((Number) row.get("count")).intValue());
    }

    model.addAttribute("labels", labels);
    model.addAttribute("counts", counts);

    return "admin-dashboard";
  }

  // ================= STUDENTS LIST + SEARCH =================
  @GetMapping("/students")
  public String students(
      @RequestParam(required = false) String keyword,
      HttpSession session,
      Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username"));
    model.addAttribute("activePage", "students");
    model.addAttribute("keyword", keyword);

    String sql = "SELECT * FROM students";
    List<?> students;

    if (keyword != null && !keyword.isBlank()) {
      sql += " WHERE name ILIKE ? OR email ILIKE ? OR course ILIKE ?";
      String k = "%" + keyword + "%";

      students = jdbcTemplate.queryForList(sql, k, k, k);
    } else {
      students = jdbcTemplate.queryForList(sql);
    }

    model.addAttribute("students", students);

    return "admin-students";
  }

  // ================= ADD STUDENT PAGE =================
  @GetMapping("/students/add")
  public String addStudentPage(HttpSession session, Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username")); // ⭐ REQUIRED
    model.addAttribute("activePage", "students");

    // ⭐ ALSO REQUIRED for dropdown
    model.addAttribute("courses",
        jdbcTemplate.queryForList("SELECT * FROM courses"));

    return "admin-add-student";
  }

  // ================= ADD STUDENT SUBMIT =================
  @PostMapping("/students/add")
  public String addStudent(
      @RequestParam String name,
      @RequestParam String email,
      @RequestParam String course,
      @RequestParam String username,
      @RequestParam String password,
      RedirectAttributes ra) {

    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM students WHERE username=?",
        Integer.class,
        username);

    if (count != null && count > 0) {
      ra.addFlashAttribute("error", "Username already exists");
      return "redirect:/students/add";
    }

    jdbcTemplate.update(
        "INSERT INTO students(name,email,course,username,password) VALUES (?,?,?,?,?)",
        name, email, course, username, password);

    ra.addFlashAttribute("message", "Student added successfully");
    return "redirect:/students";
  }

  // ================= EDIT STUDENT PAGE =================
  @GetMapping("/students/edit/{id}")
  public String editStudent(
      @PathVariable int id,
      HttpSession session,
      Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username")); // ⭐ ADD THIS
    model.addAttribute("activePage", "students");

    model.addAttribute("student",
        jdbcTemplate.queryForMap("SELECT * FROM students WHERE id=?", id));

    model.addAttribute("courses",
        jdbcTemplate.queryForList("SELECT course_name FROM courses"));

    return "admin-edit-student";
  }

  // ================= UPDATE STUDENT =================
  @PostMapping("/students/update")
  public String updateStudent(
      @RequestParam int id,
      @RequestParam String name,
      @RequestParam String email,
      @RequestParam String course,
      RedirectAttributes ra) {

    jdbcTemplate.update(
        "UPDATE students SET name=?, email=?, course=? WHERE id=?",
        name, email, course, id);

    ra.addFlashAttribute("message", "Student updated successfully");
    return "redirect:/students";
  }

  // ================= DELETE STUDENT =================
  @PostMapping("/students/delete")
  public String deleteStudent(
      @RequestParam int id,
      RedirectAttributes ra) {

    jdbcTemplate.update("DELETE FROM students WHERE id=?", id);
    ra.addFlashAttribute("message", "Student deleted successfully");
    return "redirect:/students";
  }

  // ================= COURSES =================
  @GetMapping("/courses")
  public String courses(HttpSession session, Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username"));
    model.addAttribute("activePage", "courses");

    model.addAttribute("courses",
        jdbcTemplate.queryForList("SELECT * FROM courses"));

    return "admin-courses";
  }

  // ================= ADD COURSE PAGE =================
  @GetMapping("/courses/add")
  public String addCoursePage(HttpSession session, Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username")); // ⭐ ADD
    model.addAttribute("activePage", "courses");

    return "admin-add-course";
  }

  // ================= ADD COURSE SUBMIT =================
  @PostMapping("/courses/add")
  public String addCourse(
      @RequestParam String name,
      @RequestParam String description,
      RedirectAttributes ra) {

    jdbcTemplate.update(
        "INSERT INTO courses(course_name, description) VALUES (?, ?)",
        name, description);

    ra.addFlashAttribute("message", "Course added successfully");
    return "redirect:/courses";
  }

  @GetMapping("/courses/edit/{id}")
  public String editCourse(
      @PathVariable int id,
      HttpSession session,
      Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username")); // ⭐ ADD
    model.addAttribute("activePage", "courses");

    model.addAttribute("course",
        jdbcTemplate.queryForMap("SELECT * FROM courses WHERE id=?", id));

    return "admin-edit-course";
  }

  @PostMapping("/courses/update")
  public String updateCourse(
      @RequestParam int id,
      @RequestParam String name,
      @RequestParam String description,
      RedirectAttributes ra) {

    jdbcTemplate.update(
        "UPDATE courses SET course_name=?, description=? WHERE id=?",
        name, description, id);

    ra.addFlashAttribute("message", "Course updated successfully");
    return "redirect:/courses";
  }

  // ================= DELETE COURSE =================
  @PostMapping("/courses/delete")
  public String deleteCourse(
      @RequestParam int id,
      RedirectAttributes ra) {

    jdbcTemplate.update("DELETE FROM courses WHERE id=?", id);

    ra.addFlashAttribute("message", "Course deleted successfully");
    return "redirect:/courses";
  }

}
