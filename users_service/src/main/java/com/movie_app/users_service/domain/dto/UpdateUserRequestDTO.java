package com.movie_app.users_service.domain.dto;

import com.movie_app.users_service.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequestDTO {
    private String email;
    private String phone;
    private UserRole userRole;

}