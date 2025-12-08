package com.clinicapp.appointment.controller;

import com.clinicapp.appointment.model.Patient;
import com.clinicapp.appointment.repository.PatientRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientRepository.save(patient));
        
    }

    @GetMapping
       public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

@GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable Long id) {
    return patientRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

}

    
}
