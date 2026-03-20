package com.example.OrderingFood.service.specification;

import com.example.OrderingFood.model.Order;
import com.example.OrderingFood.model.dto.OrderFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification <Order> hasName(OrderFilterRequestDTO order){
        return (root, query, cb)->{
            if(order.getName()==null){
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), order.getName());
        };
    }
}
