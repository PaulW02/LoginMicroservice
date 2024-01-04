package com.example.loginmicroservice.services;

import com.example.loginmicroservice.dtos.UserDTO;
import com.example.loginmicroservice.entities.User;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.BadRequestException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.loginmicroservice.KeycloakConfig.*;

@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    // Keycloak Admin REST API credentials (same as in the previous example)
    // ...

    public Keycloak getKeycloakInstance() {
        ResteasyClientBuilder clientBuilder = (ResteasyClientBuilder) ResteasyClientBuilder.newBuilder();
        return KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_SERVER_URL)
                .grantType(OAuth2Constants.PASSWORD)
                .realm(REALM_NAME)
                .clientId("admin-cli")
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .build();
    }

    @Override
    public void addRoleToUser(String userId, String roleName) {
        try {
            Keycloak keycloak = getKeycloakInstance();
            // Get the user resource
            UserResource userResource = keycloak.realm(REALM_NAME).users().get(userId);

            // Get the role resource
            RoleResource roleResource = keycloak.realm(REALM_NAME).roles().get(roleName);
            // Add the role to the user
            userResource.roles().realmLevel().add(Arrays.asList(roleResource.toRepresentation()));

        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                BadRequestException bre = (BadRequestException) e;
                String responseBody = bre.getResponse().readEntity(String.class);
                System.out.println("Keycloak Server Response: " + responseBody);
            }
            e.printStackTrace();
        }
    }

    @Override
    public UserDTO getKeycloakUser(String userId) {
        Keycloak keycloak = getKeycloakInstance();
        UserResource userResource = keycloak.realm(REALM_NAME).users().get(userId);
        return new UserDTO(userResource.toRepresentation().getId(), userResource.toRepresentation().getFirstName(), userResource.toRepresentation().getLastName(), userResource.toRepresentation().getEmail());
    }

    @Override
    public List<UserDTO> getAllByRole(String role) {
        Keycloak keycloak = getKeycloakInstance();
        UsersResource userResource = keycloak.realm(REALM_NAME).users();
        RoleResource roleResource = keycloak.realm(REALM_NAME).roles().get(role);
        // Get users with the specified role
        List<UserRepresentation> usersWithRole = userResource.list()
                .stream()
                .filter(user -> userResource.get(user.getId()).roles() != null &&
                        userResource.get(user.getId()).roles().realmLevel().listAll().contains(roleResource.toRepresentation()))
                .toList();

        // Convert UserRepresentation to UserDTO
        List<UserDTO> userDTOList = usersWithRole.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());

        return userDTOList;
    }


    @Override
    public Map<String, String> getUsersByIds(List<String> userIds) {
        Keycloak keycloak = getKeycloakInstance();
        UsersResource userResource = keycloak.realm(REALM_NAME).users();

        return userIds.stream()
                .collect(Collectors.toMap(
                        userId -> userId,
                        userId -> {
                            // Fetch each user individually based on the user ID
                            UserRepresentation user = userResource.get(userId).toRepresentation();
                            return (user != null) ? user.getFirstName() + " " + user.getLastName() : null;
                        }
                ));
    }
    private UserDTO convertToUserDTO(UserRepresentation userRepresentation) {
        return new UserDTO(userRepresentation.getId(), userRepresentation.getFirstName(), userRepresentation.getLastName(), userRepresentation.getEmail());
    }
}
