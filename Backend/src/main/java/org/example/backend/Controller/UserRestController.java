package org.example.backend.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.backend.DTO.User.*;
import org.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
@Validated
public class UserRestController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    @Autowired
    public UserRestController(UserService userService, UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping()
    public List<UserResponse> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("/staff")
    public List<UserResponse> getStaffUsers() {
        return userService.findAllStaff().stream()
                .map(userResponseMapper)
                .collect(Collectors.toList());
    }
    @GetMapping("/customers")
    public List<UserResponse> getCustomerUsers() {
        return userService.findAllCustomers().stream()
                .map(userResponseMapper)
                .collect(Collectors.toList());
    }
    /*@GetMapping("/staff/{staffId}")
    public UserResponse getStaffUserById(@PathVariable("staffId") @Positive Long staffId) {
        return userService.findUserById(staffService.findById(staffId));
    }*/
    @PostMapping("/auth/login")
    public UserResponse findUserForLogin(@Valid @RequestBody UserLoginRequest user) {
        return userService.findUserForLogin(user);
    }

    @PostMapping("/register")
    public UserResponse addUser(@Valid @RequestBody UserRegistrationRequest userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@Valid @PathVariable("userId") @Positive Long userId, @Valid @RequestBody UserUpdateRequest userDto) {
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") @Positive Long userId) {
        userService.remove(userId);
    }
}
