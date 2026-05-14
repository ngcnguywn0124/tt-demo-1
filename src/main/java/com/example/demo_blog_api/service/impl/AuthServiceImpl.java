package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.dto.ApiMapper;
import com.example.demo_blog_api.dto.AuthResponse;
import com.example.demo_blog_api.dto.LoginRequest;
import com.example.demo_blog_api.dto.RegisterRequest;
import com.example.demo_blog_api.dto.UserResponse;
import com.example.demo_blog_api.entity.Role;
import com.example.demo_blog_api.entity.RoleName;
import com.example.demo_blog_api.entity.User;
import com.example.demo_blog_api.entity.UserStatus;
import com.example.demo_blog_api.exception.DuplicateResourceException;
import com.example.demo_blog_api.exception.ResourceNotFoundException;
import com.example.demo_blog_api.repository.RoleRepository;
import com.example.demo_blog_api.repository.UserRepository;
import com.example.demo_blog_api.security.JwtUtils;
import com.example.demo_blog_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userDetails.getUsername()));
        String token = jwtUtils.generateToken(userDetails);
        return new AuthResponse(token, "Bearer", jwtUtils.getExpirationMillis(), ApiMapper.toUserResponse(user));
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists: " + request.email());
        }

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + RoleName.USER));

        User user = new User();
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName().trim());
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);

        return ApiMapper.toUserResponse(userRepository.save(user));
    }
}
