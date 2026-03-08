package com.clinicapp.appointment.service;

import com.clinicapp.appointment.dto.DoctorRequest;
import com.clinicapp.appointment.dto.DoctorResponse;
import com.clinicapp.appointment.exceptions.UsernameNotFoundException;
import com.clinicapp.appointment.model.Doctor;
import com.clinicapp.appointment.model.User;
import com.clinicapp.appointment.repository.DoctorRepository;
import com.clinicapp.appointment.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    UserRepo userRepo;

    public DoctorResponse createDoctor(DoctorRequest request){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setAvailableFrom(request.getAvailableFrom());
        doctor.setAvailableTo(request.getAvailableTo());


        doctor = doctorRepository.save(doctor);

        user.setDoctor(doctor);
        userRepo.save(user);

        return mapToDoctorResponse(doctor);
    }
    @Cacheable(value = "doctors")
    public List<DoctorResponse> getAllDoctors(){
        var doctor =  doctorRepository.findAll();
        return doctor.stream()
                .map(this::mapToDoctorResponse)
                .toList();
    }

    @Cacheable(value = "doctor", key = "#id")
    public DoctorResponse getDoctorById(Long id){
        var doctor = doctorRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Doctor is not exists"));
        return mapToDoctorResponse(doctor);
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor){
        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getSpecialization(),
                doctor.getAvailableFrom(),
                doctor.getAvailableTo()
        );
    }
}
