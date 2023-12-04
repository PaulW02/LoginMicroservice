package com.example.loginmicroservice.dtos;

import com.example.loginmicroservice.entities.User;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private int age;

    public UserDTO(Long id, String firstName, String lastName, String email,String role, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.age = age;
    }

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),user.getRoles(), user.getAge());
    }

    public static User fromUserDTO(UserDTO user) {
        return new User(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getAge(), user.getRole());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
