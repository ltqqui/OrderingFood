package com.example.OrderingFood.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString

public class UserFilterRequestDTO {
    private String name ;
    private String address;
    private String email;
}
