package com.example.OrderingFood.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoodFilterRequestDTO {
    private Long id;
    private String name;
    private Integer status;
}
