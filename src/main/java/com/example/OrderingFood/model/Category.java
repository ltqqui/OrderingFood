package com.example.OrderingFood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    private int status;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "category")
    private List<Food> foods;

    @PrePersist
    public void beforeCreate(){
        this.createdAt= Instant.now();
        this.updatedAt= Instant.now();
    }

}
