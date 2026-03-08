package com.clinicapp.appointment.controller;


import com.clinicapp.appointment.dto.AppointmentRequest;
import com.clinicapp.appointment.dto.RescheduleRequest;
import com.clinicapp.appointment.model.*;
import com.clinicapp.appointment.repository.DoctorRepository;
import com.clinicapp.appointment.repository.AppointmentRepository;
import com.clinicapp.appointment.repository.UserRepo;
import com.clinicapp.appointment.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

@Autowired
private DoctorRepository doctorRepository;
@Autowired
private AppointmentRepository appointmentRepository; 
@Autowired
   private EmailService emailService;
@Autowired
    UserRepo userRepo;


// create appointment 
   @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName();

       User user = userRepo.findByUsername(username).orElseThrow();
       Patient patient = user.getPatient();
    //  Validation is patient and doctor exist
    Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
    if (doctor==null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doctor not found");
    }


    //  Validation is appointment time is within doctor's working hours
    LocalDateTime appointmentTime = request.getAppointmentTime();
    LocalTime appointmentTimeOnly = appointmentTime.toLocalTime();

    if (appointmentTimeOnly.isBefore(doctor.getAvailableFrom()) || appointmentTimeOnly.isAfter(doctor.getAvailableTo())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Appointment time is outside the doctor’s working hours");
    }

    //  Checking for double booking
    boolean exists = appointmentRepository.findByDoctorAndAppointmentTime(
            doctor, appointmentTime).isPresent();



    if (exists) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message",
                            "Slot already booked",
                            "suggestions",
                            getSuggestedSlots(request.getDoctorId(),  appointmentTime))
                        );
    }

    //  Creating new appointment
    Appointment appointment = new Appointment();
    appointment.setPatient(patient);
    appointment.setDoctor(doctor);
    appointment.setAppointmentTime(appointmentTime);
    appointment.setStatus(Status.SCHEDULED);
    Appointment saved = appointmentRepository.save(appointment);

       //send a mail appointment details
    emailService.sendAppointmentEmail(saved);

    return ResponseEntity.ok(saved);





}



// Reschedule appointment
   @PutMapping("/reschedule/{id}")
   @CacheEvict(value = "appointments",key = "#id")
public ResponseEntity<?> rescheduleAppointment(
           @PathVariable Long id,
           @RequestBody RescheduleRequest rescheduleRequest
           ) {
    Appointment appointment = appointmentRepository.findById(id).orElse(null);
    if (appointment == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment not found");
    }
    LocalDateTime newAppointmentTime = rescheduleRequest.getNewAppointmentTime();
    Doctor doctor = appointment.getDoctor();
    LocalTime newTime = newAppointmentTime.toLocalTime();
    
    if(newTime.isBefore(doctor.getAvailableFrom()) || newTime.isAfter(doctor.getAvailableTo())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("New appointment time is outside the doctor’s working hours");
    }
    boolean exists = appointmentRepository.findByDoctorAndAppointmentTime(
            doctor, newAppointmentTime).isPresent();
    if (exists) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message",
                            "Slot already booked",
                            "suggestions",
                            getSuggestedSlots(doctor.getId(), newAppointmentTime))
                        );
                    }
                        appointment.setAppointmentTime(newAppointmentTime);
    Appointment saved = appointmentRepository.save(appointment);

    // rescheduled mail
       emailService.sendAppointmentEmail(saved);

    return ResponseEntity.ok(saved);
    
    }


        


    // cancel request 
    @DeleteMapping("/cancel/{id}")
    @CacheEvict(value = "appointments",key = "#id")
    public ResponseEntity<?> cancelAppointment(@PathVariable  Long id) {
     Appointment appointment =  appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment not found");
        }

    appointment.setStatus(Status.CANCELED);
    Appointment saved = appointmentRepository.save(appointment);

    // send mail for canceled request
        emailService.sendAppointmentEmail(saved);

    return ResponseEntity.ok(saved);

    }
   
    // return ResponseEntity.ok(Map.of("message", "Appointment rescheduled successfully"));}


    // suggested slots every 15 minutes within next 2 hours
    @GetMapping("/slotsuggestion")
    @CacheEvict(value = "appointments",key = "#doctorId")
    private List<LocalDateTime> getSuggestedSlots(Long doctorId, LocalDateTime requestedTime) {
    List<LocalDateTime> suggestions = new ArrayList<>();    
        
    LocalDateTime start = requestedTime.plusMinutes(15);
    LocalDateTime end = requestedTime.plusHours(2);
    while (start.isBefore(end)) {
        boolean exists = appointmentRepository.findByDoctorIdAndAppointmentTime(
                doctorId, start).isPresent();
        if (!exists) {
            suggestions.add(start);
        }
        start = start.plusMinutes(15);
    }
    // sent mail for suggested slots
    return suggestions;
    }

    @GetMapping("/getAppointments")
    @Cacheable(value = "appointments")
    public List<Appointment> getAppointments(){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName();

        User user = userRepo.findByUsername(username).orElseThrow();
        Doctor doctor = user.getDoctor();

        return appointmentRepository.findByDoctor(doctor);

    }

}
