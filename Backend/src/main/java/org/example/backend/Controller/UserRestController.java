package org.example.backend.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.backend.DTO.User.*;
import org.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
@Validated
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserResponse> getUsers() {
        //get current authenticated username (typically email)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth != null ? auth.getName() : null;

        //retrieve all users and filter out the currently logged-in user (by email)
        return userService.findAll().stream()
                .filter(u -> currentEmail == null || !currentEmail.equals(u.email()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.findUserById(userId);
    }
    
    @PostMapping("/auth/login")
    public UserResponse findUserForLogin(@Valid @RequestBody UserLoginRequest user) {
        return userService.findUserForLogin(user);
    }

    @PostMapping("/register")
    public UserResponse addUser(@Valid @RequestBody UserRegistrationRequest userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable("userId") @Positive Long userId, @Valid @RequestBody UserUpdateRequest userDto) {
        return userService.update(userId, userDto);
    }

    @PutMapping("/admin/{userId}")
    public UserResponse updateUserAsAdmin(@Valid @PathVariable("userId") @Positive Long userId, @Valid @RequestBody AdminUserUpdateRequest adminDto) {
        return userService.updateAsAdmin(userId, adminDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") @Positive Long userId) {
        userService.remove(userId);
    }
}
