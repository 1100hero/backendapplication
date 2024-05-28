package org.techstage.backendapplication.service.api;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.token.TokenDTO;
import org.techstage.backendapplication.model.token.email.EmailSender;
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

    public void sendResetPswRequest(TokenDTO tokenDTO) {
        var user = userRepository.findUserByConfirmedToken(tokenDTO.token());
        if (user.isEmpty()) return;
        emailSender.send(user.get().getEmail(), EmailService
                        .buildEmailResetPsw(user.get().getName(),
                                "ciaooo"),
                "Ripristina la password.");
    }
}
