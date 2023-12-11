package com.example.loginmicroservice.controllers;

import com.example.loginmicroservice.dtos.CreatePatientDTO;
import com.example.loginmicroservice.dtos.CreateUserDTO;
import com.example.loginmicroservice.dtos.UserAuthenticationDTO;
import com.example.loginmicroservice.dtos.UserDTO;
import com.example.loginmicroservice.entities.User;
import com.example.loginmicroservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private final WebClient patientClient;

    public UserController(WebClient.Builder webClientBuilder) {
        this.patientClient = webClientBuilder.baseUrl("http://patient-microservice-service:5003/patient").build();
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        User createdUser = userService.createUser(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getPassword(), createUserDTO.getEmail(), createUserDTO.getAge(), createUserDTO.getRoles());
        if (createUserDTO.getRoles().contains("Patient")){
            patientClient.post()
                    .uri("/")
                    .bodyValue(new CreatePatientDTO(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getAge(), createdUser.getId()))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }
        return ResponseEntity.ok(UserDTO.fromUser(createdUser));
    }

    @GetMapping("/names")
    public Map<Long, String> getUserNamesByIds(@RequestParam("userIds") List<Long> userIds) {
        return userService.getUsersByIds(userIds);
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

    @GetMapping("/checkUser/{id}")
    public ResponseEntity<Long> checkUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromUser(user).getId());
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
