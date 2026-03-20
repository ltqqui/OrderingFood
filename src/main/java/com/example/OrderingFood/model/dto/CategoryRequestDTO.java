package com.example.OrderingFood.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequestDTO {
    private int id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    @NotBlank(message = "Miêu tả không được để trống")
    private String description;

    private int status;

}
