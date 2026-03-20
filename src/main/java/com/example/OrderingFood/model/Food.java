package com.example.OrderingFood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên món không được để trống")
    private String name;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải >=0 ")
    private int quantity;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Số lượng phải >=0 ")
    private double price;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotBlank(message = "Hình ảnh tả không được để trống")
    private String img;

    private int status;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "food")
    private List<OrderItem> orderItem;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "food")
    private List<CartItem> cartItem;

    @PrePersist
    public void beforeCreate(){
        this.createdAt= Instant.now();
        this.updatedAt= Instant.now();
    }

}
