package com.clinicapp.appointment.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;


@Entity

public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @Setter
    @ManyToOne
    @JoinColumn(name = "doctor_id",nullable = false)
    private Doctor doctor;
    
    @Setter
    @NotNull
    private LocalDateTime appointmentTime;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;// e.g. "scheduled", "canceled", "completed"

   

      public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        this.status = Status.SCHEDULED;
    }   

    public Long getId() {
        return id;
    }   

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public Status getStatus() {
        return status;
    }


}