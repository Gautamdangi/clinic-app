package com.clinicapp.appointment.repository;

import com.clinicapp.appointment.model.Appointment;
import com.clinicapp.appointment.model.Doctor;

import ch.qos.logback.core.status.Status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Finding existing appointment by doctor and appointment time
    //Optional<Appointment> findByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime appointmentTime);
    Optional<Appointment> findByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);

    List<Appointment> findByStatus(Status status);

  //  Optional<Doctor> findByDoctorIdAndAppointmentTime(Doctor doctor, LocalDateTime newAppointmentTime);

    Appointment save(Appointment appointment);

    Optional<Object> findByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime start);

    List<Appointment> findByDoctor(Doctor doctor);

    //Appointment findByStatus(Status status);
}