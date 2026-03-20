package com.example.OrderingFood.service;

import com.example.OrderingFood.config.FileService;
import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.Category;
import com.example.OrderingFood.model.Food;
import com.example.OrderingFood.model.dto.FoodFilterRequestDTO;
import com.example.OrderingFood.model.dto.FoodRequestDTO;
import com.example.OrderingFood.model.dto.FoodResponseDTO;
import com.example.OrderingFood.repository.CategoryRepository;
import com.example.OrderingFood.repository.FoodRepository;
import com.example.OrderingFood.service.specification.FoodSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public Food convertDTOToFood(FoodRequestDTO foodRequestDTO, String fileName){
        Category category= this.categoryRepository.findById(foodRequestDTO.getCategoryId()).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thầy danh mục")
        );
        return Food.builder()
                .name(foodRequestDTO.getName())
                .description(foodRequestDTO.getDescription())
                .img(fileName)
                .quantity(foodRequestDTO.getQuantity())
                .price(foodRequestDTO.getPrice())
                .status(foodRequestDTO.getStatus())
                .category(category)
                .build();
    }

    public FoodResponseDTO convertFoodToDTO(Food food){
        return FoodResponseDTO.builder()
                .id(food.getId())
                .name(food.getName())
                .quantity(food.getQuantity())
                .price(food.getPrice())
                .description(food.getDescription())
                .img(food.getImg())
                .status(food.getStatus())
                .createdAt(food.getCreatedAt())
                .updatedAt(food.getUpdatedAt())
                .build();
    }

    public FoodResponseDTO createFood(FoodRequestDTO inputFood) throws IOException {
        String fileName = fileService.save(inputFood.getImg());
        Food food = this.foodRepository.save(convertDTOToFood(inputFood, fileName));
        return this.convertFoodToDTO(food);
    }

    public Page<FoodResponseDTO> getFoods(Pageable pageable, FoodFilterRequestDTO foodFilterRequestDTO){
        Specification<Food> specs= Specification.allOf(
                FoodSpecification.hasName(foodFilterRequestDTO)
        );
        Page<FoodResponseDTO> foodList= this.foodRepository.findAll(specs, pageable).map(food->
                this.convertFoodToDTO(food)
                );
        return foodList;
    }

    public FoodResponseDTO getFoodById(Long id){
        Food food = this.foodRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy món này")
                );
        return this.convertFoodToDTO(food);
    }

    public FoodResponseDTO updateFood(Long id , FoodRequestDTO foodUpdate) throws IOException {
        Food food= this.foodRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy món này")
                );
        if (foodUpdate.getImg() != null && !foodUpdate.getImg().isEmpty()) {
            String fileName = fileService.save(foodUpdate.getImg());
            food.setImg(fileName);
        }

        // Update các field
        food.setName(foodUpdate.getName());
        food.setDescription(foodUpdate.getDescription());
        food.setQuantity(foodUpdate.getQuantity());
        food.setPrice(foodUpdate.getPrice());
        food.setStatus(foodUpdate.getStatus());

        Category category = this.categoryRepository.findById(foodUpdate.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
        food.setCategory(category);
        Food foodUpdated = this.foodRepository.save(food);
        return this.convertFoodToDTO(foodUpdated);
    }

    public void deleteFood (Long id){
        this.foodRepository.deleteById(id);
    }

}
