package com.example.OrderingFood.config;

import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.RefreshToken;
import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.ExchangeTokenResponse;
import com.example.OrderingFood.model.dto.LoginResponseDTO;
import com.example.OrderingFood.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenService refreshTokenService;

    @Value("${orderingfood.jwt.access-token-validity-in-seconds}")
    private String accessTokenExpiration;

    @Value("${orderingfood.jwt.refresh-token-validity-in-seconds}")
    private String refreshTokenExpiration;

    public String getScope(Authentication authentication) {
        if (authentication != null) {
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            return scope;
        }
        return "UNKNOW";
    }

    public String generateSecureToken() {
        byte[] randomBytes = new byte[64]; // 512 bits
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public String createRefreshToken (User user){
        Instant now = Instant.now();
        Instant validity = now.plus(Long.valueOf(this.refreshTokenExpiration), ChronoUnit.SECONDS);
        String token= generateSecureToken();
        RefreshToken rf = new RefreshToken();
        rf.setCreatedAt(now);
        rf.setExpiredAt(validity);
        rf.setToken(token);
        rf.setUser(user);
        this.refreshTokenService.createRefreshToken(rf);
        return token;
    }


    public String createAccessToken(Authentication authentication, Long userId) {
        Instant now = Instant.now();
        Instant validity = now.plus(Long.valueOf(this.accessTokenExpiration), ChronoUnit.SECONDS);

        // ghép các quyền thành 1 string: "ROLE_USER ROLE_ADMIN"

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("id", userId)
                .claim("scope", this.getScope(authentication))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public ExchangeTokenResponse handleExchangeToken(String inputToken){
        RefreshToken currentRefreshToken= this.refreshTokenService.findByToken(inputToken);

        Instant now= Instant.now();
        if(now.isAfter(currentRefreshToken.getExpiredAt())){
            throw new ResourceNotFoundException("Refresh token đã hết hạn");
        }

        User currentUser = currentRefreshToken.getUser();
        String newRefreshToken= this.createRefreshToken(currentUser);
        Instant validity = now.plus(Long.valueOf(this.accessTokenExpiration), ChronoUnit.SECONDS);

        // ghép các quyền thành 1 string: "ROLE_USER ROLE_ADMIN"

        String scope = "ROLE_" + currentUser.getRole().getName();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(currentUser.getLastName()+ currentUser.getFirstName())
                .claim("id", currentUser.getId())
                .claim("scope", scope)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        String accessToken = this.jwtEncoder.encode(
                JwtEncoderParameters.from(jwsHeader, claims)
        ).getTokenValue();

        ExchangeTokenResponse exToken= new ExchangeTokenResponse();
        exToken.setAccessToken(accessToken);
        exToken.setRefreshToken(newRefreshToken);
        exToken.setUser(new LoginResponseDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), scope, currentUser.getFirstName(), currentUser.getLastName()));

        this.refreshTokenService.deleteById(currentRefreshToken.getId());
        return exToken;
    }

}
