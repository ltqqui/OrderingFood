package com.example.OrderingFood.repository;

import com.example.OrderingFood.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByIdOrName(Integer id, String name);
}
