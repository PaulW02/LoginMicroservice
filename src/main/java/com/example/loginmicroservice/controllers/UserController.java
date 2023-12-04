package com.example.loginmicroservice.controllers;

import com.example.loginmicroservice.dtos.CreateUserDTO;
import com.example.loginmicroservice.dtos.UserAuthenticationDTO;
import com.example.loginmicroservice.dtos.UserDTO;
import com.example.loginmicroservice.entities.User;
import com.example.loginmicroservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        User createdUser = userService.createUser(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getPassword(), createUserDTO.getEmail(), createUserDTO.getAge(), createUserDTO.getRoles());
        return ResponseEntity.ok(UserDTO.fromUser(createdUser));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        List<User> users = userService.getAllPatients();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<UserDTO>> getAllDoctors() {
        List<User> users = userService.getAllDoctors();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserDTO>> getAllEmployees() {
        List<User> users = userService.getAllEmployees();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserAuthenticationDTO userAuthenticationDTO) {
        if (userAuthenticationDTO.getUsername() != null && userAuthenticationDTO.getPassword() != null) {
            User loginUser = userService.loginUser(userAuthenticationDTO.getUsername(), userAuthenticationDTO.getPassword());
            if (loginUser != null) {
                return ResponseEntity.ok(UserDTO.fromUser(loginUser));
            }
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
