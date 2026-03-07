package org.example.backend.Service;

import jakarta.transaction.Transactional;
import org.example.backend.Model.entity.Role;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User findUserForLogin(String email, String password) {
        Optional<User> user = userRepository.findUserByEmailAndPassword(email, password);
        return user.orElseThrow(() -> new IllegalArgumentException("Wrong email or password"));
    }

    List<User> findUsersByRole(Role role) {
        return userRepository.findUsersByRole(role);
    }

    List<User> findAllStaff() {
        return userRepository.findAllStaff();
    }

    @Transactional
    public void remove(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

}
