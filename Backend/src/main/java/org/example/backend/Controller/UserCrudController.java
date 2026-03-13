package org.example.backend.Controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.UserDTO;
import org.example.backend.DTO.UserRegistrationDTO;
import org.example.backend.Model.entity.User;
import org.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserCrudController {

    private final UserService userService;
    
    @Autowired
    public UserCrudController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{userId}")
    public UserDTO getUser(@PathVariable("userId") Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping("/register")
    public User addUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        return userService.create(userDTO);
    }
}
