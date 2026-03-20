package com.example.OrderingFood.model.dto;

import com.example.OrderingFood.model.OrderItem;
import com.example.OrderingFood.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;

    @NotNull(message = "Tổng hóa đơn không được để trống")
    @Min(value = 0, message = "Phải >=0")
    private double totalAmount;

    private int status;

    private Instant createdAt;

    private Instant updatedAt;

    private OutputUser user;

    private List<OutputItem> orderItems;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class OutputUser{
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class OutputItem {
        private Long foodId;
        private int quantity;
        private double price;
    }

}
