package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.AuthResponse;
import com.example.demo_blog_api.dto.LoginRequest;
import com.example.demo_blog_api.dto.RegisterRequest;
import com.example.demo_blog_api.dto.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserResponse register(RegisterRequest request);
}
