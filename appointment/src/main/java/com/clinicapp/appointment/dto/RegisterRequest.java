package com.clinicapp.appointment.dto;

import com.clinicapp.appointment.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private Role role;

    private String name;
    private String phone;
    private String email;

    private String specialization;


}
