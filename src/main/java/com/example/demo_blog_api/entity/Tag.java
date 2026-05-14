package com.example.demo_blog_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tags")
@Setter
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được quá 100 ký tự")
    @Column(unique = true, columnDefinition = "varchar(100)")
    private String name;

    private String slug;
}
