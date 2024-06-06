package org.techstage.backendapplication.service.api;

import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.dto.EmailDTO;
import org.techstage.backendapplication.model.dto.TokenDTO;
import org.techstage.backendapplication.model.token.email.EmailSender;
import org.techstage.backendapplication.model.dto.UpdateUserDTO;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.token.EmailService;

@Service
public class ApiService {

    private final UserRepository userRepository;
    private final EmailSender emailSender;

    public ApiService(UserRepository userRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public void sendResetPswRequest(TokenDTO dto) {
        var user = userRepository.findUserByConfirmedToken(dto.token());
        if (user.isEmpty()) return;
        var email = userRepository.findUserByConfirmedToken(dto.token()).get().getEmail();
        var link = "http://51.254.36.232:8085/api/reset?email=" + email;
        emailSender.send(
                user.get().getEmail(),
                EmailService.buildEmailResetPsw(user.get().getName(), link),
                "Ripristina la password.");
    }

    public void sendResetPswForgotRequest(String email) {
        var user = userRepository.findUserByEmail(email);
        if(user.isEmpty()) return;
        if(!user.get().isEnabled()) return;
        var emailReset = userRepository.findUserByEmail(email).get().getEmail();
        var link = "http://51.254.36.232:8085/api/reset?email=" + emailReset;
        System.out.println(link);
        emailSender.send(
                user.get().getEmail(),
                EmailService.buildEmailResetPsw(user.get().getName(), link),
                "Ripristina la password."
        );
    }

    public void update(UpdateUserDTO updateUserDTO, Integer id) {
        userRepository.updateUserByName(id, updateUserDTO.name());
        userRepository.updateUserBySurname(id, updateUserDTO.surname());
        userRepository.updateUserByTelephone(id, updateUserDTO.telephone());
    }
}
