package com.example.OrderingFood.service.specification;

import com.example.OrderingFood.model.CartItem;
import com.example.OrderingFood.model.dto.CartItemFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class CartSpecification {
    public static Specification<CartItem> hasName(CartItemFilterRequestDTO cartItem){
        return (root, query, cb)->{
            if(cartItem.getName()==null){
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("food").get("name")),
                    "%" + cartItem.getName().toLowerCase() + "%"
            );
        };
    }
}
