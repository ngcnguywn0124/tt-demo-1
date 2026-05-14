package com.example.demo_blog_api.repository;

import com.example.demo_blog_api.entity.Role;
import com.example.demo_blog_api.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
