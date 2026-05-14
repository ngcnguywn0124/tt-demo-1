package com.example.demo_blog_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/home/demo")
    public String demo(){
        return "Hello world";
    }
}
