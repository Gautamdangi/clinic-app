package com.clinicapp.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
public class Doctor {
   
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Getter
    @NotBlank(message = "Doctor name is mandatory")
    private String name;

    @Setter
    @Getter
    @NotBlank(message = "Department is mandatory")
    private String department;

    public Doctor() {}// is this needed? // default constructor

    public Doctor(String name, String department) { // constructor
        this.name = name;
        this.department = department;
    }



    private LocalTime availableFrom;
    public LocalTime getAvailableFrom() {
       
        return availableFrom;
    }
    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    @Getter
    @Setter
    private LocalTime availableTo;


}