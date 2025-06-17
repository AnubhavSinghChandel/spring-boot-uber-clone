package com.project.taxiGo.taxiGoApp.services.implementation;

import com.project.taxiGo.taxiGoApp.entities.User;
import com.project.taxiGo.taxiGoApp.exceptions.ResourceNotFoundException;
import com.project.taxiGo.taxiGoApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: " + email + " does not exist!"));
    }

    public User getUserById(Long id){
        return userRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User with id: "+id+" does not exist!"));
    }
}
