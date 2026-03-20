package com.example.OrderingFood.service;

import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.RefreshToken;
import com.example.OrderingFood.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken (RefreshToken rf){
        this.refreshTokenRepository.save(rf);
    }

    public RefreshToken findByToken(String token){
        return this.refreshTokenRepository.findByToken(token).orElseThrow(()->
                new ResourceNotFoundException("Token not found")
                );
    }

    public void deleteById(Long id){
        this.refreshTokenRepository.deleteById(id);
    }

}
