package com.example.OrderingFood.service;

import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.Cart;
import com.example.OrderingFood.model.CartItem;
import com.example.OrderingFood.model.Food;
import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.CartItemFilterRequestDTO;
import com.example.OrderingFood.model.dto.CartRequestDTO;
import com.example.OrderingFood.model.dto.CartResponseDTO;
import com.example.OrderingFood.repository.CartItemRepository;
import com.example.OrderingFood.repository.CartRepository;
import com.example.OrderingFood.repository.FoodRepository;
import com.example.OrderingFood.repository.UserRepository;
import com.example.OrderingFood.service.specification.CartSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final CartItemRepository cartItemRepository;





    public CartResponseDTO converCartToDTO(Cart inputCart) {
        return CartResponseDTO.builder()
                .id(inputCart.getId())
                .totalPrice(inputCart.getTotalPrice())
                .user(
                        new CartResponseDTO.OutputUser(inputCart.getUser().getId(), inputCart.getUser().getLastName() + " " + inputCart.getUser().getFirstName())
                )
                .cartItems(
                        inputCart.getCartItems().stream().map((item) ->
                                new CartResponseDTO.OutputCartItems(
                                        item.getFood().getId(),
                                        item.getFood().getPrice(),
                                        item.getFood().getQuantity()
                                )
                        ).collect(Collectors.toList())
                )
                .createdAt(inputCart.getCreatedAt())
                .updatedAt(inputCart.getUpdatedAt())
                .build();
    }


    public CartResponseDTO addCartItem(CartRequestDTO inputCart) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Long userId = jwt.getClaim("id");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Lazy create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalPrice(1);
                    return cartRepository.save(newCart);
                });

        double totalPrice = cart.getTotalPrice();

        for (CartRequestDTO.InputCartItems item : inputCart.getItemList()) {

            Food food = foodRepository.findById(item.getFoodId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Không tìm thấy món với id: " + item.getFoodId())
                    );

            if (item.getQuantity() > food.getQuantity()) {
                throw new RuntimeException("Số lượng không đủ cung cấp");
            }

            Optional<CartItem> existingItem = cart.getCartItems()
                    .stream()
                    .filter(ci -> ci.getFood().getId().equals(food.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(
                        existingItem.get().getQuantity() + item.getQuantity()
                );
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setFood(food);
                cartItem.setPrice(food.getPrice());
                cartItem.setQuantity(item.getQuantity());

                cart.getCartItems().add(cartItem);
                totalPrice += food.getPrice() * item.getQuantity();
            }
        }

        cart.setTotalPrice(totalPrice);

        return converCartToDTO(cartRepository.save(cart));
    }


    public Page<CartResponseDTO.OutputCartItems> getCarts(Pageable page, CartItemFilterRequestDTO filter) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Long userId = jwt.getClaim("id");

        Cart cartInDB = this.cartRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy người dùng")
        );

        Specification<CartItem> specs = Specification.allOf(
                CartSpecification.hasName(filter),
                (root, query, cb) -> cb.equal(root.get("cart").get("id"), cartInDB.getId())
        );

        Page<CartResponseDTO.OutputCartItems> cartItems = this.cartItemRepository.findAll(specs, page).map(cartItem -> {
            return new CartResponseDTO.OutputCartItems(
                    cartItem.getFood().getId(),
                    cartItem.getFood().getPrice(),
                    cartItem.getFood().getQuantity()
            );
        });
        return cartItems;
    }

    public void updateCartItem(int option, Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Long userId = jwt.getClaim("id");
        CartItem cartItem = this.cartItemRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy cart  item ")
        );
        Cart cartInDB = this.cartRepository.findByUserId(userId).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy ")
        );
        if (id != cartInDB.getId()) {
            throw new ResourceNotFoundException("Không có quyền cập nhật");
        }
        cartItem.setQuantity(cartItem.getQuantity() + option);
        this.cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Long userId = jwt.getClaim("id");
        Cart cartInDB = this.cartRepository.findByUserId(userId).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy ")
        );
        CartItem cartItem= this.cartItemRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy cart item")
                );
        if (cartItem.getId() != cartInDB.getId()) {
            throw new ResourceNotFoundException("Không có quyền xóa");
        }

        this.cartItemRepository.deleteById(id);
    }

}
