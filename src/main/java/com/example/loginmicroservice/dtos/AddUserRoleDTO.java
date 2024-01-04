package com.example.loginmicroservice.dtos;

import org.springframework.web.bind.annotation.RequestParam;

public class AddUserRoleDTO {
    private String userId;

    private String firstName;
    private String lastName;
    private String roleName;

    public AddUserRoleDTO(String userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
