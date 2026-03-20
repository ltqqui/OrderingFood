package com.example.OrderingFood.model.dto;

import com.example.OrderingFood.model.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponseDTO {

    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    private String lastName;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    @NotNull(message = "Trạng thái không được để trống")
    private int status;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private Instant createdAt;

    private Instant updatedAt;

    private RoleResponseDTO role;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class OutputOrder {
    private Long id;
    private double totalAmount;
    }

}
