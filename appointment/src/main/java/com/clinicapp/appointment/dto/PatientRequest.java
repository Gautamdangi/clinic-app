package com.clinicapp.appointment.dto;

import com.clinicapp.appointment.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {

    private String name;
    private String email;
    private String phone;
    private Integer age;
    private Gender gender;



}
