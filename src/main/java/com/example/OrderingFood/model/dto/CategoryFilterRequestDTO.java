package com.example.OrderingFood.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class CategoryFilterRequestDTO {
    private int id;
    private String name;
}
