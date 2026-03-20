package com.example.OrderingFood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name="orderItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Số lượng không được bỏ trống")
    @Min(value = 0, message = "Phải >= 0")
    private int quantity;

    @NotNull(message = "Giá không được bổ trống")
    @Min(value = 0, message = "Phải >= 0")
    private double price;

    private int status;

    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;


    @PrePersist
    public void beforeCreate(){
        this.createdAt= Instant.now();
        this.updatedAt= Instant.now();
    }
}
