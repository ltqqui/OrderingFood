package com.example.OrderingFood.service.specification;

import com.example.OrderingFood.model.Category;
import com.example.OrderingFood.model.dto.CategoryFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> hasName(CategoryFilterRequestDTO filter){
        return (root, query, cb)->{
            if(filter.getName()==null){
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), filter.getName());
        };
    }
}
