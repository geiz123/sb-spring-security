package com.example.demo.ctrl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CtrlOne {

    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello/{id}")
    public String one(@PathVariable Long id) {
        Integer i = jdbcTemplate.queryForObject("SELECT count(*) FROM INFORMATION_SCHEMA.USERS", Integer.class);

        return i + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @PostMapping("/bye")
    public String bye() {
        return "bye";
    }

    @GetMapping("/admin")
    public String admin() {
        return "You're the admin!";
    }
}