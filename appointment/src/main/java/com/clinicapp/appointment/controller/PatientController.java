package com.clinicapp.appointment.controller;

import com.clinicapp.appointment.dto.PatientRequest;
import com.clinicapp.appointment.dto.PatientResponse;


import com.clinicapp.appointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.createPatient(request));
        
    }

    @GetMapping("/getAll")
       public List<PatientResponse> getAllPatients() {
        return patientService.getAllPatient();
    }

@GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable Long id) {
    return  ResponseEntity.ok(patientService.getById(id));

}

    
}
