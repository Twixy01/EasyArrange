package org.example.backend.Controller;

import org.example.backend.DTO.User.UserResponse;
import org.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String getAllUsers(Model model) {
        List<UserResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return "users-list";
    }
}
