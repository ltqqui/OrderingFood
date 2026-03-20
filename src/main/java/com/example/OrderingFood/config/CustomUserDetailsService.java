package com.example.OrderingFood.config;

import com.example.OrderingFood.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.Collections;

public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService){
        this.userService= userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.OrderingFood.model.User myUser=this.userService.findUserByEmail(username);
        if(myUser==null){
            throw new UsernameNotFoundException("User not found");
        }
        return new User(
                myUser.getEmail(),
                myUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" +myUser.getRole().getName())));

    }
}
