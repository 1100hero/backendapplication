package org.techstage.backendapplication.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void login(UserLoginDTO loginDTO) {

    }

    @Override
    public void save(UserRegistrationDTO registrationDTO) {
        var user = new User(registrationDTO.getName(), registrationDTO.getSurname(), registrationDTO.getTelephone(),
                registrationDTO.getEmail(), registrationDTO.getPassword(), List.of(new Role("ROLE_USER")));
        userRepository.save(user);
    }
}
