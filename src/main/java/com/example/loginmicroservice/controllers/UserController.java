package com.example.loginmicroservice.controllers;

import com.example.loginmicroservice.dtos.*;
import com.example.loginmicroservice.entities.User;
import com.example.loginmicroservice.services.KeycloakAdminService;
import com.example.loginmicroservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    private final WebClient patientClient;

    public UserController(WebClient.Builder webClientBuilder) {
        this.patientClient = webClientBuilder.baseUrl("http://patient-microservice-service:5003/patient").build();
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        User createdUser = userService.createUser(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getPassword(), createUserDTO.getEmail(), createUserDTO.getAge(), createUserDTO.getRoles());
        /*if (createUserDTO.getRoles().contains("Patient")){
            patientClient.post()
                    .uri("/")
                    .bodyValue(new CreatePatientDTO(createUserDTO.getFirstName(), createUserDTO.getLastName(), createUserDTO.getAge(), createdUser.getId()))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }*/
        return ResponseEntity.ok(UserDTO.fromUser(createdUser));
    }


    @PostMapping("/addRole")
    public ResponseEntity<String> addRoleToUser(@RequestBody AddUserRoleDTO addUserRoleDTO) {
        try {
            keycloakAdminService.addRoleToUser(addUserRoleDTO.getUserId(), addUserRoleDTO.getRoleName());
            if (addUserRoleDTO.getRoleName().contains("role_patient")){
                UserDTO userDTO = keycloakAdminService.getKeycloakUser(addUserRoleDTO.getUserId());
                patientClient.post()
                        .uri("/")
                        .bodyValue(new CreatePatientDTO(userDTO.getFirstName(), userDTO.getLastName(), 21, addUserRoleDTO.getUserId()))
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            }
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add role to user: " + e.getMessage());
        }
    }

    @GetMapping("/names")
    public Map<String, String> getUserNamesByIds(@RequestParam("userIds") List<String> userIds) {
        return keycloakAdminService.getUsersByIds(userIds);
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
        List<UserDTO> users = keycloakAdminService.getAllByRole("role_patient");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<UserDTO>> getAllDoctors() {
        List<UserDTO> users = keycloakAdminService.getAllByRole("role_doctor");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserDTO>> getAllEmployees() {
        List<UserDTO> users = keycloakAdminService.getAllByRole("role_employee");
        return ResponseEntity.ok(users);
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
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/checkUser/{id}")
    public ResponseEntity<String> checkUserById(@PathVariable String id) {
        UserDTO user = keycloakAdminService.getKeycloakUser(id);
        if (user != null) {
            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
