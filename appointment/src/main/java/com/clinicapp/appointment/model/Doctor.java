package com.clinicapp.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Entity
@Data
@Table(name = "Doctors")
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
   

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    @NotBlank(message = "Doctor name is mandatory")
    private String name;


    @Column(name = "specialization")
    @NotBlank(message = "Specialization is mandatory")
    private String specialization;

    private String phone;

    private String email;

//    public Doctor() {}// is this needed? // default constructor




    @Column(name = "AvailableFrom")
    private LocalTime availableFrom;

    @Column(name = "AvailableTo")
    private LocalTime availableTo;


}