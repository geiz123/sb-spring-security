package com.example.demo.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CtrlOne {

    @GetMapping("/hello/{id}")
    String one(@PathVariable Long id) {
        return "hello " + id;
    }

    @PostMapping("bye")
    String bye(){
        return "bye";
    }
}