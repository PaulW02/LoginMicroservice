package com.example.loginmicroservice.services;

import com.example.loginmicroservice.dtos.UserDTO;

import java.util.List;
import java.util.Map;

public interface KeycloakAdminService {
    void addRoleToUser(String userId, String roleName);
    UserDTO getKeycloakUser(String userId);

    List<UserDTO> getAllByRole(String role);
    Map<String, String> getUsersByIds(List<String> userIds);
}
