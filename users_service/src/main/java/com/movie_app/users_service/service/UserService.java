package com.movie_app.users_service.service;

import com.movie_app.users_service.domain.*;
import com.movie_app.users_service.service.observer.UserObserver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final List<UserObserver> observers;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, List<UserObserver> observers) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.observers = observers;
    }

    public void addObserver(UserObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(UserObserver observer) {
        this.observers.remove(observer);
    }

    private void notifyObservers(User user) {
        for (UserObserver observer : observers) {
            observer.onUserUpdated(user); // Apelăm interfața!
        }
    }


    public void registerUser(String username, String email, String phone, String password, UserRole role) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User(null, username, email, phone, encodedPassword, role);

        userRepository.save(user);
    }

    public User login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!bCryptPasswordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(Integer id, String newEmail, String newPhone, UserRole newRole) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found!"));

        User updatedUser = new User(
                existingUser.getId(),
                existingUser.getUsername(),
                newEmail,
                newPhone,
                existingUser.getPasswordHash(),
                newRole
        );

        User savedUser = userRepository.save(updatedUser);

        for (UserObserver observer : observers) {
            observer.onUserUpdated(savedUser);
        }

        return savedUser;
    }

    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't delete a user that doesn't exist!"));

        userRepository.deleteById(id);
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
}
