package org.example.backend.Controller;

import org.example.backend.Model.entity.User;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class UserCrudController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();

    @Autowired
    public UserCrudController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("User not found")
        );
    }

    @PostMapping("/register")
    public User addUser(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
