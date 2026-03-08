package com.clinicapp.appointment.dto;

import com.clinicapp.appointment.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private long doctorId;

    private LocalDateTime appointmentTime;
    private Status status;
}
