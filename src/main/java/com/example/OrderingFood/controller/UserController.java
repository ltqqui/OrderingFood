package com.example.OrderingFood.controller;

import com.example.OrderingFood.helper.ApiResponse;
import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.UserFilterRequestDTO;
import com.example.OrderingFood.model.dto.UserRequestDTO;
import com.example.OrderingFood.model.dto.UserResponseDTO;
import com.example.OrderingFood.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid  @RequestBody User user){
        UserResponseDTO userCreated= this.userService.createUser(user);
        return ApiResponse.created(userCreated);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getUsers(
            UserFilterRequestDTO userFilter,
            Pageable pageable
    ){
        Page<UserResponseDTO> userList= this.userService.getUsers(pageable, userFilter);
        return ApiResponse.success(userList, "Lấy danh sách thành công");
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequestDTO userUpdate){
        this.userService.updateUser(id, userUpdate);
        return ApiResponse.success(null, "Cập nhật thành công");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id){
        this.userService.deleteUser(id);
        return ApiResponse.success(null, "Xóa thành công");
    }
}
