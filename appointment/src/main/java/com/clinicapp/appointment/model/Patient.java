package com.clinicapp.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@Table(name = "Patients")
@NoArgsConstructor // Lombok's @NoArgsConstructor generates the default constructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patient name is mandatory")
    private String name;

    @NotBlank
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Phone number is REQUIRED")// add format
    private String phone;

    // Lombok's @NoArgsConstructor generates the default constructor
//
//    public Patient(String name, String email, String phone) { // constructor
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//    }
//    public Long getId() {
//        return id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getEmail() {
//        return email;
//    }
//    public void setEmail(String email) {
//        this.email = email;
//    }
//    public String getPhone() {
//        return phone;
//    }
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//

}
