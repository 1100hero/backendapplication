package org.techstage.backendapplication.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.domain.Role;
import org.techstage.backendapplication.domain.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.web.dto.UserRegistrationDTO;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;

    @Override
    public User save(UserRegistrationDTO registrationDTO) {
        var user = new User(registrationDTO.getName(), registrationDTO.getSurname(), registrationDTO.getTelephone(),
                registrationDTO.getEmail(), registrationDTO.getPassword(), List.of(new Role("ROLE_USER")));

        return userRepository.save(user);
    }
}
