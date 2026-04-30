package org.example.backend.Service;
import org.example.backend.DTO.Role.RoleResponse;
import org.example.backend.DTO.User.UserLoginRequest;
import org.example.backend.DTO.User.UserRegistrationRequest;
import org.example.backend.DTO.User.UserRegistrationRequestMapper;
import org.example.backend.DTO.User.UserResponse;
import org.example.backend.DTO.User.UserResponseMapper;
import org.example.backend.DTO.User.UserUpdateRequest;
import org.example.backend.DTO.User.UserUpdateRequestMapper;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.RoleRepository;
import org.example.backend.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private UserResponseMapper userResponseMapper;
    @Mock private UserRegistrationRequestMapper userRegistrationRequestMapper;
    @Mock private UserUpdateRequestMapper userUpdateRequestMapper;
    @Mock private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleRepository, userResponseMapper, userRegistrationRequestMapper, userUpdateRequestMapper, passwordEncoder);
    }
    private Role role(Long id, String name) {
        Role role = new Role();
        role.setRoleId(id);
        role.setName(name);
        return role;
    }
    private User user(Long id, String email, String password, Role role) {
        User user = new User();
        user.setId(id);
        user.setName("User " + id);
        user.setEmail(email);
        user.setPhoneNumber("+36301234567");
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
    @Test
    void create_encodesPasswordAndSavesUser() {
        Role role = role(2L, "CUSTOMER");
        UserRegistrationRequest request = new UserRegistrationRequest("New User", "new@example.com", "+36301234567", "plain", new RoleResponse(role.getRoleId(), role.getName()));
        User mapped = new User();
        mapped.setName(request.name());
        mapped.setEmail(request.email());
        mapped.setPhoneNumber(request.phoneNumber());
        mapped.setRole(role);
        when(userRepository.emailExists(request.email())).thenReturn(false);
        when(userRegistrationRequestMapper.apply(request)).thenReturn(mapped);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userResponseMapper.apply(any(User.class))).thenReturn(new UserResponse(1L, request.name(), request.email(), request.phoneNumber(), null, new RoleResponse(role.getRoleId(), role.getName())));
        UserResponse actual = userService.create(request);
        assertThat(actual.email()).isEqualTo(request.email());
        assertThat(mapped.getPassword()).isEqualTo("encoded-password");
        assertThat(mapped.getRole()).isSameAs(role);
        verify(userRepository).save(mapped);
    }
    @Test
    void findUserForLogin_throwsOnWrongPassword() {
        Role role = role(2L, "CUSTOMER");
        User stored = user(1L, "login@example.com", "encoded", role);
        when(userRepository.findUserByEmail("login@example.com")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("bad", "encoded")).thenReturn(false);
        assertThatThrownBy(() -> userService.findUserForLogin(new UserLoginRequest("login@example.com", "bad")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wrong email or password");
    }
    @Test
    void findUserForLogin_returnsMappedUserWhenPasswordMatches() {
        Role role = role(2L, "CUSTOMER");
        User stored = user(1L, "login@example.com", "encoded", role);
        UserResponse expected = new UserResponse(1L, "User 1", "login@example.com", "+36301234567", null, new RoleResponse(role.getRoleId(), role.getName()));
        when(userRepository.findUserByEmail("login@example.com")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("secret", "encoded")).thenReturn(true);
        when(userResponseMapper.apply(stored)).thenReturn(expected);
        UserResponse actual = userService.findUserForLogin(new UserLoginRequest("login@example.com", "secret"));
        assertThat(actual).isSameAs(expected);
    }
    @Test
    void update_changesPasswordAndRole() {
        Role oldRole = role(2L, "CUSTOMER");
        Role newRole = role(3L, "STAFF");
        User stored = user(1L, "old@example.com", "encoded-old", oldRole);
        UserUpdateRequest request = new UserUpdateRequest("Updated", "updated@example.com", "+36301234567", "current", "newpass", "avatar.png", new RoleResponse(newRole.getRoleId(), newRole.getName()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("current", "encoded-old")).thenReturn(true);
        when(userRepository.emailExists("updated@example.com")).thenReturn(false);
        when(roleRepository.findById(newRole.getRoleId())).thenReturn(Optional.of(newRole));
        when(passwordEncoder.encode("newpass")).thenReturn("encoded-new");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userResponseMapper.apply(any(User.class))).thenReturn(new UserResponse(1L, request.name(), request.email(), request.phoneNumber(), request.profilePicture(), new RoleResponse(newRole.getRoleId(), newRole.getName())));
        UserResponse actual = userService.update(1L, request);
        assertThat(actual.email()).isEqualTo(request.email());
        assertThat(stored.getPassword()).isEqualTo("encoded-new");
        assertThat(stored.getRole()).isSameAs(newRole);
        verify(userRepository).save(stored);
    }
    @Test
    void update_rejectsWrongCurrentPassword() {
        User stored = user(1L, "old@example.com", "encoded-old", role(2L, "CUSTOMER"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("wrong", "encoded-old")).thenReturn(false);
        assertThatThrownBy(() -> userService.update(1L, new UserUpdateRequest("Updated", "updated@example.com", "+36301234567", "wrong", null, null, new RoleResponse(2L, "CUSTOMER"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Current password is incorrect");
    }
}