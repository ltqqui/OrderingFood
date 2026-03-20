package com.example.OrderingFood.controller;

import com.example.OrderingFood.helper.ApiResponse;
import com.example.OrderingFood.model.dto.CartItemFilterRequestDTO;
import com.example.OrderingFood.model.dto.CartRequestDTO;
import com.example.OrderingFood.model.dto.CartResponseDTO;
import com.example.OrderingFood.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<ApiResponse<String>> createCart(@Valid @RequestBody CartRequestDTO inputCart){
        CartResponseDTO cartResponseDTO= this.cartService.addCartItem(inputCart);
        return ApiResponse.success(null, "Thêm vào giỏ hàng thành công");
    }

    @GetMapping("/carts")
    public ResponseEntity<ApiResponse<Page<CartResponseDTO.OutputCartItems>>> getCarts(
            CartItemFilterRequestDTO filter,
            Pageable pageable
    ){
        Page<CartResponseDTO.OutputCartItems> cartItems= this.cartService.getCarts(pageable, filter);
        return ApiResponse.success(cartItems, "Lấy thành công");
    }

    @PutMapping("/carts/{id}/{option}")
    public ResponseEntity<ApiResponse<String>> updateCartItem( @PathVariable int option, @PathVariable Long id){
        System.out.println(option);
        this.cartService.updateCartItem(option, id);
        return ApiResponse.success(null, "Cập nhật thành công");
    }


    @DeleteMapping("/carts/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCartItem (@PathVariable Long id){
        this.cartService.deleteCartItem(id);
        return ApiResponse.success(null,"Xóa thành công");
    }

}
