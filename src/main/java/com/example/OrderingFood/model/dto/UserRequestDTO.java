package com.example.OrderingFood.model.dto;

import com.example.OrderingFood.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@Builder

public class UserRequestDTO {
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    private String lastName;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotNull(message = "Trạng thái không được để trống")
    private int status;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private Instant createdAt;

    private Instant updatedAt;

    private Role role;
}
