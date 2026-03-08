package com.clinicapp.appointment.service;

import com.clinicapp.appointment.dto.PatientRequest;
import com.clinicapp.appointment.dto.PatientResponse;
import com.clinicapp.appointment.exceptions.UsernameNotFoundException;
import com.clinicapp.appointment.model.Gender;
import com.clinicapp.appointment.model.Patient;
import com.clinicapp.appointment.model.User;
import com.clinicapp.appointment.repository.PatientRepository;
import com.clinicapp.appointment.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PatientRepository patientRepository;



    public Patient createPatient(PatientRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepo.findByUsername(username).orElseThrow(()->new RuntimeException("user not found"));

        if(user.getPatient() != null){
            throw new RuntimeException("Patient already exists");

        }
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setAge(request.getAge());
        Gender gender = request.getGender();
        patient.setGender(gender);

        patient = patientRepository.save(patient);
        user.setPatient(patient);
        userRepo.save(user);

        return patient;
    }
    @Cacheable("patients")
    public List<PatientResponse> getAllPatient(){
      List<Patient> patient=  patientRepository.findAll();
      return patient.stream()
              .map(this::mapToPatientResponse)
              .toList();
    }
    @Cacheable(value = "patient",key = "#id")
    public PatientResponse getById(Long id){
     var patient = patientRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Patient not found with this id"));
return mapToPatientResponse(patient);

    }

    private PatientResponse mapToPatientResponse(Patient patient){
        return new PatientResponse(
                patient.getEmail(),
                patient.getName(),
                patient.getGender()
        );
    }



}
