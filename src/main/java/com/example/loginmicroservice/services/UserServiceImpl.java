package com.example.loginmicroservice.services;

import com.example.loginmicroservice.entities.User;
import com.example.loginmicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /*
    @Autowired
    private PatientRepository patientRepository;
*/
    @Override
    public User createUser(String firstname, String lastname, String password, String email, int age, String roles) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user;
        if (firstname != null && lastname != null && password != null && email != null && age > 0 && roles != null) {
            user = new User(firstname, lastname, encryptedPassword, email, age, roles);
            user = userRepository.save(user);
            /*
            if (roles.equals("Patient")) {
                Patient patient = new Patient(firstname, lastname, age);
                patient.setUser(user);
                patientRepository.save(patient);
            }*/
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            updatedUser.setId(id);
            return userRepository.save(updatedUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Long id) {
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

