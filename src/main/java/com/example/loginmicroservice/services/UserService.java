package com.example.loginmicroservice.services;

import com.example.loginmicroservice.entities.User;

import java.util.List;

public interface UserService {
    User createUser(String firstname, String lastname, String password, String email, int age, String roles);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    boolean deleteUser(Long id);

    User loginUser(String username, String password);

    List<User> getAllPatients();

    List<User> getAllDoctors();

    List<User> getAllEmployees();
}
