package com.example.loginmicroservice.repositories;

import com.example.loginmicroservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2")
    User findUserByEmailAndPassword(String email, String password);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.roles = 'Patient'")
    List<User> findAllPatients();
    @Query("SELECT u FROM User u WHERE u.roles = 'Doctor'")
    List<User> findAllDoctors();
    @Query("SELECT u FROM User u WHERE u.roles = 'Worker'")
    List<User> findAllEmployees();
}
