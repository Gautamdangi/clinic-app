package com.clinicapp.appointment.dto;

import com.clinicapp.appointment.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String email;
    private String name;
    private Gender gender;

}
