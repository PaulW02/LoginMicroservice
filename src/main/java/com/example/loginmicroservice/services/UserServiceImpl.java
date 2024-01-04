package com.example.loginmicroservice.services;

import com.example.loginmicroservice.entities.User;
import com.example.loginmicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public User createUser(String firstname, String lastname, String password, String email, int age, String roles) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user;
        if (firstname != null && lastname != null && password != null && email != null && age > 0 && roles != null) {
            user = new User(firstname, lastname, encryptedPassword, email, age, roles);
            user = userRepository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public Map<String, String> getUsersByIds(List<String> userIds) {
        // Fetch users in batch using userRepository.findAllById
        List<User> users = userRepository.findAllById(userIds);

        // Ensure the order of the result matches the order of the input userIds
        return userIds.stream()
                .collect(Collectors.toMap(
                        userId -> userId,
                        userId -> users.stream()
                                .filter(user -> user.getId().equals(userId))
                                .findFirst()
                                .map(user -> user.getFirstName() + " " + user.getLastName())
                                .orElse(null)
                ));
    }

    @Override
    public User updateUser(String id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            updatedUser.setId(id);
            return userRepository.save(updatedUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public User loginUser(String username, String password) {
        User user = userRepository.findUserByEmail(username);

        if (user != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllPatients() {
        return userRepository.findAllPatients();
    }

    @Override
    public List<User> getAllDoctors() {
        return userRepository.findAllDoctors();
    }

    @Override
    public List<User> getAllEmployees() {
        return userRepository.findAllEmployees();
    }
}

