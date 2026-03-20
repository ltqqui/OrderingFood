package com.example.OrderingFood.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
    private int id;
    private String name;
    private int status;
    private Instant createdAt;
    private Instant updatedAt;
}
