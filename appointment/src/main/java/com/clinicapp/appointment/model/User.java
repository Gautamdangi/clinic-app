package com.clinicapp.appointment.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String username; //  email or phone

        @Column(nullable = false)
        private String password; // BCrypt hashed

        @Enumerated(EnumType.STRING)
        private Role role;

        // optional: link to patient/doctor profile
        @OneToOne
        @JoinColumn(name = "patient_id")
        private Patient patient;

        @OneToOne
        @JoinColumn(name = "doctor_id")
        private Doctor doctor;
    }

