package com.example.OrderingFood.model.dto;

import com.example.OrderingFood.model.CartItem;
import com.example.OrderingFood.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartRequestDTO {
//    @NotNull(message = "Tổng tiền không được bỏ trống")
//    @Min(value = 0, message = "Phải >=0")

    private List<InputCartItems> itemList;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class InputCartItems{
        private Long foodId;
        private double price;
        private int quantity;
    }
}
