package com.example.OrderingFood.controller;

import com.example.OrderingFood.helper.ApiResponse;
import com.example.OrderingFood.model.dto.OrderFilterRequestDTO;
import com.example.OrderingFood.model.dto.OrderRequestDTO;
import com.example.OrderingFood.model.dto.OrderResponseDTO;
import com.example.OrderingFood.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO inputOrder){
        OrderResponseDTO orderResponseDTO= this.orderService.createOrder(inputOrder);
        return ApiResponse.created(orderResponseDTO);
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrders(
        OrderFilterRequestDTO filter,
        Pageable page
    ){
    Page<OrderResponseDTO> orderList= this.orderService.getOrders(page, filter);
    return ApiResponse.success(orderList, "Lấy danh sách thành công");
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id){
        OrderResponseDTO orderResponseDTO= this.orderService.getOrderById(id);
        return ApiResponse.success(orderResponseDTO, "Lấy hóa đơn thành công");
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable  Long id){
        this.orderService.deteOrder(id);
        return ApiResponse.success(null, "Xóa thành công");
    }

}
