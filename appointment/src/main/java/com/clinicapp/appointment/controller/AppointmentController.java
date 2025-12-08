package com.clinicapp.appointment.controller;

import com.clinicapp.appointment.model.Appointment;
import com.clinicapp.appointment.model.Doctor;
import com.clinicapp.appointment.model.Patient;
import com.clinicapp.appointment.model.Status;
import com.clinicapp.appointment.repository.PatientRepository;
import com.clinicapp.appointment.repository.DoctorRepository;
import com.clinicapp.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
private PatientRepository patientRepository;

@Autowired
private DoctorRepository doctorRepository;

@Autowired
private AppointmentRepository appointmentRepository; 

    

// create appointment 

   @PostMapping("/create")
public ResponseEntity<?> createAppointment(
    @RequestParam long doctorId,
    @RequestParam long patientId,
    @RequestParam String status,
    @RequestParam LocalDateTime appointmentTime
) 
    {

    //  Validation is patient and doctor exist
    Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
    if (doctor==null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doctor not found");
    }

    
    Patient patient = patientRepository.findById(patientId).orElse(null);
    if (patient== null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Patient not found");
    }

    //  Validation is appointment time is within doctor's working hours

    LocalTime appointmentTimeOnly = appointmentTime.toLocalTime();

    if (appointmentTimeOnly.isBefore(doctor.getAvailableFrom()) || appointmentTimeOnly.isAfter(doctor.getAvailableTo())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Appointment time is outside the doctor’s working hours");
    }

    //  Checking for double booking
    Boolean exists = appointmentRepository.findByDoctorIdAndAppointmentTime(
            doctor, appointmentTime).isPresent();

    if (exists) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message",
                            "Slot already booked",
                            "suggestions",
                            getSuggestedSlots(doctorId, appointmentTime))
                        );
    }

    //  Creating new appointment
    Appointment appointment = new Appointment();
    appointment.setPatient(patient);
    appointment.setDoctor(doctor);
    appointment.setAppointmentTime(appointmentTime);
    Appointment saved = appointmentRepository.save(appointment);
    return ResponseEntity.ok(saved);
}



// Reschedule appointment
   @PutMapping("/reschedule/{id}")
public ResponseEntity<?> rescheduleAppointment(
    @PathVariable Long id,
    @RequestParam LocalDateTime newAppointmentTime
) {
    Appointment appointment = (Appointment) appointmentRepository.findById(id).orElse(null);
    if (appointment == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment not found");
    }    
    Doctor doctor = appointment.getDoctor();
    LocalTime newTime = newAppointmentTime.toLocalTime();
    
    if(newTime.isBefore(doctor.getAvailableFrom()) || newTime.isAfter(doctor.getAvailableTo())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("New appointment time is outside the doctor’s working hours");
    }
    Boolean exists = appointmentRepository.findByDoctorIdAndAppointmentTime(
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
                    
    return ResponseEntity.ok(saved);
    
    }


        


    // cancel request 

    public ResponseEntity<?> cancelAppointment(@PathVariable @org.springframework.lang.NonNull Long id) {
     Appointment appointment = (Appointment) appointmentRepository.findById(id).orElse(null);      
        if (appointment == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment not found");
        }

    appointment.setStatus(Status.CANCELED);
    Appointment saved = appointmentRepository.save(appointment);
    return ResponseEntity.ok(saved);

    }
   
    // return ResponseEntity.ok(Map.of("message", "Appointment rescheduled successfully"));}


    // suggested slots every 15 minutes within next 2 hours
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
    return suggestions;
    }

}
