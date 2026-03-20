package com.example.OrderingFood.service;

import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.Food;
import com.example.OrderingFood.model.Order;
import com.example.OrderingFood.model.OrderItem;
import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.OrderFilterRequestDTO;
import com.example.OrderingFood.model.dto.OrderRequestDTO;
import com.example.OrderingFood.model.dto.OrderResponseDTO;
import com.example.OrderingFood.repository.FoodRepository;
import com.example.OrderingFood.repository.OrderRepository;
import com.example.OrderingFood.repository.UserRepository;
import com.example.OrderingFood.service.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public OrderResponseDTO convertFoodToDTO(Order order){
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .user(new OrderResponseDTO.OutputUser(order.getUser().getId(), order.getUser().getLastName() + " " +  order.getUser().getFirstName()))
                .orderItems(order.getOrderItems().stream().map((item)->
                                new OrderResponseDTO.OutputItem(item.getFood().getId(), item.getQuantity(), item.getPrice())
                        ).collect(Collectors.toList()))
                .build();
    }

    public OrderResponseDTO createOrder(OrderRequestDTO inputOrder){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Long userId = jwt.getClaim("id");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));


        Order order= new Order();
        order.setUser(user);
        order.setStatus(0);
        double totalAmount= 0;
        List<OrderItem> orderItems= new ArrayList<>();
        for (OrderRequestDTO.InputItem item: inputOrder.getOrderItems()){
            Food food= this.foodRepository.findById(item.getFoodId()).orElseThrow(()->
                    new ResourceNotFoundException("Không tìm thấy món với id là: " + item.getFoodId())
                    );

            if(item.getQuantity() > food.getQuantity()){
                throw new RuntimeException("Số lượng không đủ cung cấp");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setFood(food);
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(food.getPrice());
            orderItem.setStatus(0);
            totalAmount += food.getPrice() * item.getQuantity();
            orderItems.add(orderItem);

            food.setQuantity(food.getQuantity()- item.getQuantity());
        }
            order.setOrderItems(orderItems);
            order.setTotalAmount(totalAmount);
            this.orderRepository.save(order);
            return this.convertFoodToDTO(order);
    }

    public Page<OrderResponseDTO> getOrders(Pageable page, OrderFilterRequestDTO filter){
        Specification<Order> specs= Specification.allOf(
                OrderSpecification.hasName(filter)
        );
        Page<OrderResponseDTO> orderList= this.orderRepository.findAll(specs, page).map(order->
                this.convertFoodToDTO(order)
                );

        return orderList;
    }

    public OrderResponseDTO getOrderById(Long id){
        Order order= this.orderRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy hóa đơn")
                );
        return this.convertFoodToDTO(order);
    }

//    public OrderResponseDTO updateOrder

    public void deteOrder(Long id){this.orderRepository.deleteById(id);}

}
