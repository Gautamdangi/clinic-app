package com.clinicapp.appointment.controller;

import com.clinicapp.appointment.dto.DoctorRequest;
import com.clinicapp.appointment.dto.DoctorResponse;
import com.clinicapp.appointment.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {


    @Autowired
    DoctorService doctorService;

    @PostMapping("/create")
   public ResponseEntity<?> createDoctor(@RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.createDoctor(request));
    }

    @GetMapping("/getdoctors")
    public  ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));

    }
}
