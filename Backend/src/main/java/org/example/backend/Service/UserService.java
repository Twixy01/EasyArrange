package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.DTO.*;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.RoleRepository;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRegistrationRequestMapper userRegistrationRequestMapper;
    private final UserUpdateRequestMapper userUpdateRequestMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserResponseMapper userResponseMapper, UserRegistrationRequestMapper userRegistrationRequestMapper, UserUpdateRequestMapper userUpdateRequestMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRegistrationRequestMapper = userRegistrationRequestMapper;
        this.userUpdateRequestMapper = userUpdateRequestMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userResponseMapper).collect(Collectors.toList());
    }

    public UserResponse findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userResponseMapper).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User findUserForLogin(String email, String password) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("Wrong email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Wrong email or password");
        }
        return user;
    }

    public List<User> findUsersByRole(Role role) {
        return userRepository.findUsersByRole(role);
    }

    public List<User> findAllStaff() {
        return userRepository.findAllStaff();
    }

    @Transactional
    public UserResponse create(UserRegistrationRequest userDto) {
        if (userRepository.emailExists(userDto.email())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        User user = userRegistrationRequestMapper.apply(userDto);

        user.setPassword(passwordEncoder.encode(userDto.password()));

        Role role = roleRepository.findById(userDto.roleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found!"));

        user.setRole(role);

        userRepository.save(user);

        return userResponseMapper.apply(user);
    }

    @Transactional
    public UserResponse update(Long userId, UserUpdateRequest userDto) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        if (!existingUser.getEmail().equals(userDto.email()) && userRepository.emailExists(userDto.email())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        User user = userUpdateRequestMapper.apply(userDto);

        user.setId(userId);

        user.setPassword(passwordEncoder.encode(userDto.password()));

        Role role = roleRepository.findById(userDto.roleId()).orElseThrow(() -> new IllegalArgumentException("Role not found!"));
        user.setRole(role);

        userRepository.save(user);

        return userResponseMapper.apply(user);
    }

    @Transactional
    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }

}
