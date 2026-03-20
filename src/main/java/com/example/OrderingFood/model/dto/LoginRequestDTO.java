package com.example.OrderingFood.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

}
