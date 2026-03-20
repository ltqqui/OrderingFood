package com.example.OrderingFood.controller;

import com.example.OrderingFood.helper.ApiResponse;
import com.example.OrderingFood.model.Category;
import com.example.OrderingFood.model.dto.CategoryRequestDTO;
import com.example.OrderingFood.model.dto.CategoryResponseDTO;
import com.example.OrderingFood.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryRequestDTO inputCategory){
        CategoryResponseDTO categoryResponseDTO= this.categoryService.createCategory(inputCategory);
        return ApiResponse.created(categoryResponseDTO);
    }

    @GetMapping("/categoriescategories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getCategories(){
        List<CategoryResponseDTO> categoryList= this.categoryService.getCategories();
        return ApiResponse.success( categoryList);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(@PathVariable  int id, @Valid @RequestBody CategoryRequestDTO categoryUpdate){
        CategoryResponseDTO newCategory= this.categoryService.updateCategory(id, categoryUpdate);
        return ApiResponse.success(newCategory, "Cập nhật thành công");
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable int id){
        this.categoryService.deleteCategory(id);
        return ApiResponse.success(null, "Xóa thành công");
    }

}
