package com.example.demo_blog_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với user
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Refresh token
    @Column(nullable = false, unique = true)
    private String token;

    // Thời gian hết hạn
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    // Token đã bị thu hồi hay chưa
    @Column(nullable = false)
    private Boolean revoked = false;

    // Thời gian tạo
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Thời gian cập nhật
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}