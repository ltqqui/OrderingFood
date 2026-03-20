package com.example.OrderingFood.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private Long id;
    private double totalPrice;
    private OutputUser user;
    private List<OutputCartItems> cartItems;

    private Instant createdAt;

    private Instant updatedAt;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class OutputUser{
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class OutputCartItems{
        private Long foodId;
        private double price;
        private int quantity;
    }

}
