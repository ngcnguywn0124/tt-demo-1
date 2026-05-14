package com.example.demo_blog_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "categories")
@Setter @Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được quá 100 ký tự")
    @Column(unique = true, columnDefinition = "varchar(100)")
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "varchar(100)")
    private String slug;
}
