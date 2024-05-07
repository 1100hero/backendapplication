package org.techstage.backendapplication.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.domain.Role;
import org.techstage.backendapplication.domain.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.web.dto.UserLoginDTO;
import org.techstage.backendapplication.web.dto.UserRegistrationDTO;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void login(UserLoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void save(UserRegistrationDTO registrationDTO) {
        var user = new User(registrationDTO.getName(), registrationDTO.getSurname(), registrationDTO.getTelephone(),
                registrationDTO.getEmail(), passwordEncoder.encode(registrationDTO.getPassword()), List.of(new Role("ROLE_USER")));
        userRepository.save(user);
    }
}
