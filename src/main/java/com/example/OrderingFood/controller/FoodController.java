package com.example.OrderingFood.controller;

import com.example.OrderingFood.helper.ApiResponse;
import com.example.OrderingFood.model.dto.FoodFilterRequestDTO;
import com.example.OrderingFood.model.dto.FoodRequestDTO;
import com.example.OrderingFood.model.dto.FoodResponseDTO;
import com.example.OrderingFood.service.FoodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping(value = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponseDTO>> createFood(@Valid  @ModelAttribute FoodRequestDTO food) throws IOException {
        FoodResponseDTO foodResponseDTO= this.foodService.createFood(food);
        return ApiResponse.created(foodResponseDTO);
    }

    @GetMapping("/foods")
    public ResponseEntity<ApiResponse<Page<FoodResponseDTO>>> getFoods(Pageable pageable, FoodFilterRequestDTO foodFilterRequestDTO){
        Page<FoodResponseDTO> foodList= this.foodService.getFoods(pageable, foodFilterRequestDTO);
        return ApiResponse.success(foodList, "Lấy danh sách thành công");
    }

    @GetMapping("/foods/{id}")
    public ResponseEntity<ApiResponse<FoodResponseDTO>> getFoodById(@PathVariable Long id){
        FoodResponseDTO foodResponseDTO= this.foodService.getFoodById(id);
        return ApiResponse.success(foodResponseDTO, "Lấy thành công");
    }

    @PutMapping(value = "/foods/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponseDTO>> updateFood(@PathVariable Long id, @Valid @ModelAttribute FoodRequestDTO foodRequestDTO) throws IOException {
        FoodResponseDTO foodResponseDTO= this.foodService.updateFood(id, foodRequestDTO);
        return ApiResponse.success(foodResponseDTO, "Cập nhật thành công");
    }

    @DeleteMapping("/foods/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFood(@PathVariable  Long id){
        this.foodService.deleteFood(id);
        return ApiResponse.success(null, "Xóa thành công");
    }

}
