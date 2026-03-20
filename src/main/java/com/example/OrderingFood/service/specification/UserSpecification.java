package com.example.OrderingFood.service.specification;

import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.UserFilterRequestDTO;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasName(UserFilterRequestDTO userFiler){
        return (root, query, cb) ->{
            if(userFiler.getName()==null){
                return cb.conjunction();
            }
            return cb.equal(root.get("firstName"), userFiler.getName());
        };
    }
}
