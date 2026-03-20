package com.example.OrderingFood.controller;

import com.example.OrderingFood.config.JwtService;
import com.example.OrderingFood.helper.ApiResponse;
import com.example.OrderingFood.model.RefreshToken;
import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.LoginRequestDTO;
import com.example.OrderingFood.model.dto.LoginResponseDTO;
import com.example.OrderingFood.model.dto.RegisterRequestDTO;
import com.example.OrderingFood.service.RefreshTokenService;
import com.example.OrderingFood.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${orderingfood.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> postRegister(@Valid @RequestBody RegisterRequestDTO inputUser){
        this.userService.register(inputUser);
        System.out.println("dang ky roi");
        return ApiResponse.success("Đăng ký thành công");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> postLogin(@Valid @RequestBody LoginRequestDTO dto ) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        User currentUser= this.userService.findUserByEmail(authentication.getName());
        String accessToken = this.jwtService.createAccessToken(authentication, currentUser.getId());
        String refreshToken = this.jwtService.createRefreshToken(currentUser);
        LoginResponseDTO res= new LoginResponseDTO();
        res.setAccessToken(accessToken);
        res.setUser(new LoginResponseDTO.UserLogin(
                currentUser.getId(), authentication.getName(), this.jwtService.getScope(authentication), currentUser.getFirstName(), currentUser.getLastName()
        ));
        res.setRefreshToken(refreshToken);

//        set cookies

        ResponseCookie resCookies= ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        ApiResponse<LoginResponseDTO> finalData= new ApiResponse<>(HttpStatus.OK,"", res, "");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(finalData);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> postLogout(@AuthenticationPrincipal Jwt jwt, @CookieValue(required = false) String refreshToken){
        String userId= jwt.getClaimAsString("id");
        String username= jwt.getSubject();

        RefreshToken currentTokenInDB= this.refreshTokenService.findByToken(refreshToken);
        this.refreshTokenService.deleteById(currentTokenInDB.getId());


        ResponseCookie resCookies= ResponseCookie
                .from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        ApiResponse<String> finalData= new ApiResponse<>(HttpStatus.OK,"", "ok", "");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(finalData);
    }
}
