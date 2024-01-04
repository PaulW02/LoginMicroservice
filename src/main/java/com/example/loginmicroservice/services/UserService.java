package com.example.loginmicroservice.services;

import com.example.loginmicroservice.entities.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User createUser(String firstname, String lastname, String password, String email, int age, String roles);
    List<User> getAllUsers();
    User getUserById(String id);
    User updateUser(String id, User updatedUser);
    boolean deleteUser(String id);
    Map<String, String> getUsersByIds(List<String> userIds);
    User loginUser(String username, String password);

    List<User> getAllPatients();

    List<User> getAllDoctors();

    List<User> getAllEmployees();
}
