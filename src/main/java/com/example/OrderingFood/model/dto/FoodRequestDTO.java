package com.example.OrderingFood.model.dto;

import com.example.OrderingFood.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor

public class FoodRequestDTO {

    private Long id;

    @NotBlank(message = "Tên món không được để trống")
    private String name;

    @NotNull(message = "Số lượng không được để trống")
    private int quantity;

    @NotNull(message = "Giá không được để trống")
    private double price;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Hình ảnh tả không được để trống")
    private MultipartFile img;

    @NotNull(message = "Hình ảnh tả không được để trống")
    private int categoryId;

    private int status;

    private Instant createdAt;

    private Instant updatedAt;

}
