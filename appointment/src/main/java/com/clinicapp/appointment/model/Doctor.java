package com.clinicapp.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;

@Entity

public class Doctor {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Doctor name is mandatory")
    private String name;

    @NotBlank(message = "Department is mandatory")
    private String department;

    public Doctor() {}// is this needed? // default constructor

    public Doctor(String name, String department) { // constructor
        this.name = name;
        this.department = department;
    }
    public Long getId() {
        return id;
    }       
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalTime getAvailableFrom() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableFrom'");
    }



    private LocalTime availableFrom;
    private LocalTime availableTo;
    public LocalTime getAvailableTo() {
        return availableTo;
    }
    public void setAvailableTo(LocalTime availableTo) {
        this.availableTo = availableTo;
    }
    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }


}