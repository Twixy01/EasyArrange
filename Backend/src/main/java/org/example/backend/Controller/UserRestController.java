package org.example.backend.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.backend.DTO.UserResponse;
import org.example.backend.DTO.UserRegistrationRequest;
import org.example.backend.DTO.UserUpdateRequest;
import org.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping("/register")
    public UserResponse addUser(@Valid @RequestBody UserRegistrationRequest userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/users/{userId}")
    public UserResponse updateUser(@Valid @PathVariable("userId") @Positive Long userId, @Valid @RequestBody UserUpdateRequest userDto) {
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") @Positive Long userId) {
        userService.remove(userId);
    }
}
