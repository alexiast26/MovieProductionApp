package com.movie_app.users_service.infrastructure;

import com.movie_app.users_service.domain.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String phone;
    private String passwordHash;
    private UserRole userRole;
}
