package com.example.OrderingFood.service.specification;

import com.example.OrderingFood.model.Food;
import com.example.OrderingFood.model.dto.FoodFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class FoodSpecification {
    public static Specification<Food> hasName(FoodFilterRequestDTO food){
        return (root, query, cb)->{
            if(food.getName()==null){
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), food.getName());
        };
    }
}
