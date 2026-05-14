package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.UserCreateRequest;
import com.example.demo_blog_api.dto.UserResponse;
import com.example.demo_blog_api.dto.UserUpdateRequest;

import java.util.List;

public interface AdminService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse createUser(UserCreateRequest request);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
}
