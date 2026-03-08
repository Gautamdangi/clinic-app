package com.clinicapp.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {
    private String name;
    private  String email;
    private String phone;
    private String specialization;
    private LocalTime availableFrom;
    private LocalTime availableTo;

}
