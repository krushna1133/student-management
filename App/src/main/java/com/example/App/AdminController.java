package com.example.App;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  // DASHBOARD
  // @GetMapping("/admin/dashboard")
  // public String dashboard(HttpSession session, Model model) {
  //
  // if (!"ADMIN".equals(session.getAttribute("role"))) {
  // return "redirect:/login";
  // }
  //
  // model.addAttribute("name", session.getAttribute("username"));
  //
  // // total students
  // Integer total = jdbcTemplate.queryForObject(
  // "SELECT COUNT(*) FROM students", Integer.class);
  // model.addAttribute("totalStudents", total);
  //
  // // chart data
  // var rows = jdbcTemplate.queryForList(
  // "SELECT course, COUNT(*) AS count FROM students GROUP BY course");
  //
  // List<String> labels = new ArrayList<>();
  // List<Integer> counts = new ArrayList<>();
  //
  // for (var row : rows) {
  // labels.add(row.get("course").toString());
  // counts.add(((Number) row.get("count")).intValue());
  // }
  //
  // model.addAttribute("labels", labels);
  // model.addAttribute("counts", counts);
  //
  // return "admin-dashboard";
  // }

  @GetMapping("/admin/dashboard")
  public String dashboard(HttpSession session, Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username"));
    model.addAttribute("activePage", "dashboard");

    model.addAttribute("totalStudents",
        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students", Integer.class));

    // chart logic already correct
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

  // STUDENTS
  @GetMapping("/students")
  public String students(HttpSession session, Model model) {

    if (!"ADMIN".equals(session.getAttribute("role"))) {
      return "redirect:/login";
    }

    model.addAttribute("name", session.getAttribute("username"));
    model.addAttribute("activePage", "students");

    model.addAttribute("students",
        jdbcTemplate.queryForList("SELECT * FROM students"));

    return "admin-students";
  }

  // COURSES
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

}
