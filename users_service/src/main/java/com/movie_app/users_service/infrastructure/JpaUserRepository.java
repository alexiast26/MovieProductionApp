package com.movie_app.users_service.infrastructure;

import com.movie_app.users_service.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;


@org.springframework.stereotype.Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByUserRole(UserRole userRole);
}
