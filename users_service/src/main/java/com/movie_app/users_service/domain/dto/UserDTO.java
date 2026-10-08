package com.movie_app.users_service.domain.dto;

import com.movie_app.users_service.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private UserRole userRole;
}
