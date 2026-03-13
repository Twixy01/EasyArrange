package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.backend.DTO.UserDTO;
import org.example.backend.DTO.UserDTOMapper;
import org.example.backend.DTO.UserRegistrationDTO;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.RoleRepository;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserDTOMapper userDTOMapper;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDTOMapper = userDTOMapper;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper).collect(Collectors.toList());
    }

    public UserDTO findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userDTOMapper).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User findUserForLogin(String email, String password) {
        Optional<User> user = userRepository.findUserByEmailAndPassword(email, password);
        return user.orElseThrow(() -> new IllegalArgumentException("Wrong email or password"));
    }

    public List<User> findUsersByRole(Role role) {
        return userRepository.findUsersByRole(role);
    }

    public List<User> findAllStaff() {
        return userRepository.findAllStaff();
    }

    @Transactional
    public User create(@Valid UserRegistrationDTO userDto) {
        if (userRepository.emailExists(userDto.email())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        User user = new User(
                userDto.name(),
                userDto.email(),
                encoder.encode(userDto.password()),
                userDto.profilePicture(),
                roleRepository.findById(userDto.roleId()).orElseThrow(() -> new IllegalArgumentException("Role not found!"))
        );
        return userRepository.save(user);
    }

    @Transactional
    public User update(@Valid User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }

}
