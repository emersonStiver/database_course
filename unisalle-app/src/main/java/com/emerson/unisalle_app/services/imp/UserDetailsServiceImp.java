package com.emerson.unisalle_app.services.imp;

import com.emerson.unisalle_app.exceptions.ResourceNotFoundException;
import com.emerson.unisalle_app.repositories.UserRepository;
import com.emerson.unisalle_app.security.UserSecurityDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        System.out.println("FETCHING USER: " + email);
            return userRepository
                    .findByEmail(email)
                    .map(UserSecurityDetails::new)
                    .orElseThrow(() -> new ResourceNotFoundException("User: " + email + " was not found"));
    }
}
