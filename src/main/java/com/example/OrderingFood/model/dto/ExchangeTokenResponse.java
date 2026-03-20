package com.example.OrderingFood.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private LoginResponseDTO.UserLogin user ;
}
