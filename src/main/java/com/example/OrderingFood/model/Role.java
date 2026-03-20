package com.example.OrderingFood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Tên quyền không được để trống")
    private String name;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @PrePersist
    public void beforeCreate(){
        this.createdAt= Instant.now();
        this.updatedAt= Instant.now();
    }

}
