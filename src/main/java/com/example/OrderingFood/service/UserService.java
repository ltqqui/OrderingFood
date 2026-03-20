package com.example.OrderingFood.service;

import com.example.OrderingFood.helper.ResourceAlreadyExistsException;
import com.example.OrderingFood.helper.ResourceNotFoundException;
import com.example.OrderingFood.model.Role;
import com.example.OrderingFood.model.User;
import com.example.OrderingFood.model.dto.*;
import com.example.OrderingFood.repository.RoleRepository;
import com.example.OrderingFood.repository.UserRepository;
import com.example.OrderingFood.service.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy email")
        );
    }


    public void register(RegisterRequestDTO inputUser) {
        //check mail
        if (this.userRepository.existsByEmail(inputUser.getEmail())) {
            throw new ResourceAlreadyExistsException("Email đã tồn tại");
        }

//        hardcode role = User
        Role role = this.roleRepository.findByIdOrName(null, "USER").orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy role USER")
        );
        String hashPassword = this.passwordEncoder.encode(inputUser.getPassword());

        User user = new User();
        user.setPassword(hashPassword);
        user.setRole(role);
        user.setEmail(inputUser.getEmail());
        user.setFirstName(inputUser.getFirstName());
        user.setLastName(inputUser.getLastName());
        user.setPhoneNumber(inputUser.getPhoneNumber());
        user.setStatus(0);

        this.userRepository.save(user);
    }


    public UserResponseDTO convertUserToDTO(User user){
        System.out.println(user.getUpdatedAt());
    return UserResponseDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .address(user.getAddress())
            .phoneNumber(user.getPhoneNumber())
            .role(new RoleResponseDTO(user.getRole().getId(), user.getRole().getName()))
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

    public User convertDTOToPost(UserResponseDTO user){

        return null;
    }

    public UserResponseDTO createUser(User user){

        if(this.userRepository.existsByEmail(user.getEmail())){
            throw new ResourceAlreadyExistsException("Email đã tồn tại");
        }

        int roleId= user.getRole().getId();
        String roleName= user.getRole().getName();
        Role roleInDB= this.roleRepository.findByIdOrName(roleId, roleName).orElseThrow(()->
             new ResourceNotFoundException(roleName + " không tồn tại")
        );

        String hashPassword= this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(roleInDB);

        return this.convertUserToDTO(this.userRepository.save(user));
    }

    public Page<UserResponseDTO> getUsers(Pageable pageable, UserFilterRequestDTO userFilter){


        Specification<User> specs=Specification.allOf(
                UserSpecification.hasName(userFilter)
        );
        Page<UserResponseDTO> userList= this.userRepository.findAll(specs, pageable).map(user->
                this.convertUserToDTO(user)
                );

        return userList;

    }

    public void updateUser(Long id, UserRequestDTO inputUser){
        User userInDB= this.userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy user")
                );

        if(inputUser.getRole()!=null){
            userInDB.setRole(inputUser.getRole());
        }

        userInDB.setPhoneNumber(inputUser.getPhoneNumber());
        userInDB.setStatus(inputUser.getStatus());
        userInDB.setLastName(inputUser.getLastName());
        userInDB.setFirstName(inputUser.getFirstName());
        userInDB.setAddress(inputUser.getAddress());
        this.userRepository.save(userInDB);
    }

    public void deleteUser(Long id ){
        this.userRepository.deleteById(id);
    }


}
