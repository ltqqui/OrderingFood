package com.example.OrderingFood.repository;

import com.example.OrderingFood.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer > {
    Category findByName(String name);
}
