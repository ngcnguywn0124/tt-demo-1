package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.RoleName;
import com.example.demo_blog_api.entity.UserStatus;
import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
        @Email(message = "Email is invalid")
        String email,
        String fullName,
        RoleName role,
        UserStatus status
) {
}
