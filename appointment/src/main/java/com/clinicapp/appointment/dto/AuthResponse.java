package com.clinicapp.appointment.dto;

import com.clinicapp.appointment.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String username;
    private Role role;
    private String message;
    private String token;
}
