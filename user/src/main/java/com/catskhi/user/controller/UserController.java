package com.catskhi.user.controller;

import com.catskhi.user.domain.UserModel;
import com.catskhi.user.dto.UserDto;
import com.catskhi.user.mapper.UserMapper;
import com.catskhi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(String id) {
        UserModel user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = UserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserModel user = UserMapper.toModel(userDto);
        UserModel savedUser = userService.saveUser(user);
        UserDto savedUserDto = UserMapper.toDto(savedUser);
        return ResponseEntity.status(201).body(savedUserDto);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UserDto userDto) {
        UserModel userDetails = UserMapper.toModel(userDto);
        UserModel updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        UserDto updatedUserDto = UserMapper.toDto(updatedUser);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(String id) {
        UserModel deletedUser = userService.deleteUser(id);
        if (deletedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
