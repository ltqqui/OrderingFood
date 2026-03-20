package com.example.OrderingFood.model.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FoodResponseDTO {
    @Id
    private Long id;

    @NotBlank(message = "Tên món không được để trống")
    private String name;

    @NotBlank(message = "Số lượng không được để trống")
    private int quantity;

    @NotBlank(message = "Giá không được để trống")
    private double price;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotBlank(message = "Hình ảnh tả không được để trống")
    private String img;

    private int status;

    private Instant createdAt;

    private Instant updatedAt;
}
