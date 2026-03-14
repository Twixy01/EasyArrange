package org.example.backend.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new IllegalArgumentException("User not found!"));
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
    public User create(@Valid User user) {
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
