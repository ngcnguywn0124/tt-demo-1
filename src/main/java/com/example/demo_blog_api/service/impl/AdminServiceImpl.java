package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.entity.User;
import com.example.demo_blog_api.repository.RoleRepository;
import com.example.demo_blog_api.repository.UserRepository;
import com.example.demo_blog_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new User(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getRole().getName().name(),
                        user.getStatus()
                ))
                .toList();
    }
}
