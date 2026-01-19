package com.clinicapp.appointment.service;

import com.clinicapp.appointment.model.User;
import com.clinicapp.appointment.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService {

    @Autowired private UserRepo userRepo;

    public UserDetails loadUserByUsername(String username){
        User user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with this username "+ username));
        return new
                org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
