package com.clinicapp.appointment.repository;

import com.clinicapp.appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}