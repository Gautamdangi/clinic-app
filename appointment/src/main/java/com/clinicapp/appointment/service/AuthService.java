package com.clinicapp.appointment.service;

import com.clinicapp.appointment.dto.AuthResponse;
import com.clinicapp.appointment.dto.LoginRequest;
import com.clinicapp.appointment.dto.RegisterRequest;
import com.clinicapp.appointment.exceptions.UserAlreadyExistsException;
import com.clinicapp.appointment.exceptions.UsernameNotFoundException;
import com.clinicapp.appointment.model.Doctor;
import com.clinicapp.appointment.model.Patient;
import com.clinicapp.appointment.model.Role;
import com.clinicapp.appointment.model.User;
import com.clinicapp.appointment.repository.DoctorRepository;
import com.clinicapp.appointment.repository.PatientRepository;
import com.clinicapp.appointment.repository.UserRepo;
import com.clinicapp.appointment.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {



        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private PatientRepository patientRepository;

        @Autowired
        private DoctorRepository doctorRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtil jwtUtil;


        public AuthResponse register(RegisterRequest request) {
            if (userRepo.existsByUsername(request.getUsername())) {
               throw new UserAlreadyExistsException("User already sign up");
            }

            // Create User
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            Role role = request.getRole(); // expecting client to pass ROLE_PATIENT or ROLE_DOCTOR
            user.setRole(role);

            // Optionally create a Patient or Doctor profile and link
            if (role == Role.PATIENT) {
                Patient patient = new Patient();
                patient.setName(request.getUsername()); // or accept more fields in DTO
                patient.setEmail(request.getUsername());
                patient.setPhone(request.getPhone());

                patient = patientRepository.save(patient);
                user.setPatient(patient);

            }
            else if (role == Role.DOCTOR) {
                Doctor d = new Doctor();
                d.setName(request.getName());
                d.setPhone(request.getPhone());
                d.setEmail(request.getEmail());
                d.setSpecialization(request.getSpecialization());
                d = doctorRepository.save(d);
                user.setDoctor(d);
            }

           User saved = userRepo.save(user);
            String token = jwtUtil.generateToken(saved.getUsername(),saved.getRole());

            return new AuthResponse(

                    saved.getUsername(),
                    saved.getRole(),
                    "User Registered successfully with this token : ",
                    token


            );
        }

        public AuthResponse login(LoginRequest req) {

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
                );


            User user = userRepo.findByUsername(req.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User not found with this credentials"));
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            return new AuthResponse(

                    user.getUsername(),
                    user.getRole(),
                    "User Logged in  successfully with this token : ",
                    token


            );        }
//


    }


