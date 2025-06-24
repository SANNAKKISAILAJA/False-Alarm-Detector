package com.example.False.Alarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @CrossOrigin(origins = "*")
     @GetMapping("/login-success")
    public String loginSuccess() {
        // Redirect to the React app's homepage
        return "redirect:http://localhost:5183/";
    }
}