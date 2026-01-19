package com.clinicapp.appointment.controller;


import com.clinicapp.appointment.dto.AuthResponse;
import com.clinicapp.appointment.dto.LoginRequest;
import com.clinicapp.appointment.dto.RegisterRequest;
import com.clinicapp.appointment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        @Autowired
        private AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
            return ResponseEntity.ok(authService.register(req));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {

            AuthResponse authResponse = authService.login(req);
return ResponseEntity.ok(authResponse);
        }
    }
