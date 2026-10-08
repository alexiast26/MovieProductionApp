package com.movie_app.users_service.domain;

import java.util.Optional;
import java.util.List;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Integer id);
    Optional<User> findById(Integer id);
    List<User> findByRole(UserRole role);
}
