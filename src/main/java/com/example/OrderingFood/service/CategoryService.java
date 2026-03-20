package com.example.OrderingFood.service;

import com.example.OrderingFood.helper.ResourceAlreadyExistsException;
import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.Category;
import com.example.OrderingFood.model.dto.CategoryRequestDTO;
import com.example.OrderingFood.model.dto.CategoryResponseDTO;
import com.example.OrderingFood.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category convertDTOToCategory(CategoryRequestDTO categoryRequestDTO){
        return Category.builder()
                .id(categoryRequestDTO.getId())
                .name(categoryRequestDTO.getName())
                .description(categoryRequestDTO.getDescription())
                .status(categoryRequestDTO.getStatus())
                .build();
    }

    public CategoryResponseDTO convertCategoryToDTO(Category inputCategory){
        return CategoryResponseDTO.builder()
                .id(inputCategory.getId())
                .name(inputCategory.getName())
                .status(inputCategory.getStatus())
                .createdAt(inputCategory.getCreatedAt())
                .updatedAt(inputCategory.getUpdatedAt())
                .build();
    }



    public CategoryResponseDTO createCategory(CategoryRequestDTO inputCategory){
        Category category= this.categoryRepository.findByName(inputCategory.getName());
        if(category!=null){
            throw new ResourceAlreadyExistsException("Danh mục này đã tồn tại");
        }
        Category newCategory= this.categoryRepository.save(convertDTOToCategory(inputCategory));
        return this.convertCategoryToDTO(newCategory);
    }

    public List<CategoryResponseDTO> getCategories(){
        return this.categoryRepository.findAll().stream().map((cate)->
                this.convertCategoryToDTO(cate)
                ).collect(Collectors.toList());
    }

    public CategoryResponseDTO updateCategory(int id , CategoryRequestDTO categoryUpdate){
        Category categoryInDB= this.categoryRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy danh mục")
                );

        categoryInDB.setName(categoryUpdate.getName());
        categoryInDB.setDescription(categoryUpdate.getDescription());
        categoryInDB.setStatus(categoryUpdate.getStatus());
        Category newCategory= this.categoryRepository.save(categoryInDB);
        return this.convertCategoryToDTO(newCategory);
    }

    public void deleteCategory(int id){
        this.categoryRepository.deleteById(id);
    }
}
