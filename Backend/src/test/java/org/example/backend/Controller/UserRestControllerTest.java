package org.example.backend.Controller;

import org.example.backend.DTO.Role.RoleResponse;
import org.example.backend.DTO.User.*;
import org.example.backend.Model.entity.User;
import org.example.backend.Service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private UserRestController controller;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private UserResponse createResponse(Long id, String email) {
        return new UserResponse(id, "Name", email, "+36301234567", "avatar.png", new RoleResponse(2L, "CUSTOMER"));
    }

    private UserLoginRequest createLoginRequest() {
        return new UserLoginRequest("test@example.com", "pass1234");
    }

    private UserRegistrationRequest createRegistrationRequest() {
        return new UserRegistrationRequest("New User", "new@example.com", "+36301234567", "pass1234", new RoleResponse(2L, "CUSTOMER"));
    }

    private UserUpdateRequest createUserUpdateRequest() {
        return new UserUpdateRequest("Updated", "updated@example.com", "+36301234567", "oldpass", "newpass", "avatar2.png", new RoleResponse(2L, "CUSTOMER"));
    }

    private AdminUserUpdateRequest createAdminUpdateRequest() {
        return new AdminUserUpdateRequest("Admin Updated", "admin.updated@example.com", "+36301234567", "newpass", "avatar3.png", new RoleResponse(3L, "STAFF"));
    }

    @Test
    void testGetUsers_filtersCurrentAuthenticatedUserByEmail() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("me@example.com", "pw")
        );

        UserResponse me = createResponse(1L, "me@example.com");
        UserResponse other = createResponse(2L, "other@example.com");
        when(userService.findAll()).thenReturn(List.of(me, other));

        List<UserResponse> actual = controller.getUsers();

        assertThat(actual).containsExactly(other);
        verify(userService).findAll();
    }

    @Test
    void testGetUser() {
        UserResponse expected = createResponse(1L, "one@example.com");
        when(userService.findUserById(1L)).thenReturn(expected);

        UserResponse actual = controller.getUser(1L);

        assertThat(actual).isSameAs(expected);
        verify(userService).findUserById(1L);
    }

    @Test
    void testFindUserForLogin_sendsCorrectDto() {
        UserLoginRequest request = createLoginRequest();
        UserResponse response = createResponse(1L, "test@example.com");
        when(userService.findUserForLogin(any())).thenReturn(response);

        UserResponse actual = controller.findUserForLogin(request);

        ArgumentCaptor<UserLoginRequest> captor = ArgumentCaptor.forClass(UserLoginRequest.class);
        verify(userService).findUserForLogin(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testAddUser_sendsCorrectDto() {
        UserRegistrationRequest request = createRegistrationRequest();
        UserResponse response = createResponse(3L, "new@example.com");
        when(userService.create(any())).thenReturn(response);

        UserResponse actual = controller.addUser(request);

        ArgumentCaptor<UserRegistrationRequest> captor = ArgumentCaptor.forClass(UserRegistrationRequest.class);
        verify(userService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testUpdateUser_sendsCorrectDto() {
        UserUpdateRequest request = createUserUpdateRequest();
        UserResponse response = createResponse(1L, "updated@example.com");
        when(userService.update(eq(1L), any())).thenReturn(response);

        UserResponse actual = controller.updateUser(1L, request);

        ArgumentCaptor<UserUpdateRequest> captor = ArgumentCaptor.forClass(UserUpdateRequest.class);
        verify(userService).update(eq(1L), captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testUpdateUserAsAdmin_sendsCorrectDto() {
        AdminUserUpdateRequest request = createAdminUpdateRequest();
        UserResponse response = createResponse(1L, "admin.updated@example.com");
        when(userService.updateAsAdmin(eq(1L), any())).thenReturn(response);

        UserResponse actual = controller.updateUserAsAdmin(1L, request);

        ArgumentCaptor<AdminUserUpdateRequest> captor = ArgumentCaptor.forClass(AdminUserUpdateRequest.class);
        verify(userService).updateAsAdmin(eq(1L), captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDeleteUser() {
        controller.deleteUser(1L);
        verify(userService).remove(1L);
    }
}

