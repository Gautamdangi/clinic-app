package com.clinicapp.appointment.dto;

import java.time.LocalDateTime;

public class RescheduleRequest {
    private LocalDateTime newAppointmentTime;

    public RescheduleRequest() {}

    public LocalDateTime getNewAppointmentTime() {
        return newAppointmentTime;
    }

    public void setNewAppointmentTime(LocalDateTime newAppointmentTime) {
        this.newAppointmentTime = newAppointmentTime;
    }
}
