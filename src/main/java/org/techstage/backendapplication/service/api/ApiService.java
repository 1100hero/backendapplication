package org.techstage.backendapplication.service.api;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.token.TokenDTO;
import org.techstage.backendapplication.model.token.email.EmailSender;
import org.techstage.backendapplication.model.user.UpdateUserDTO;
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
        var link = "http://51.254.36.232:8085/api/reset?token="+tokenDTO.token();
        emailSender.send(
                user.get().getEmail(),
                EmailService.buildEmailResetPsw(user.get().getName(), link),
                "Ripristina la password.");
    }

    public ResponseEntity<Void> update(UpdateUserDTO updateUserDTO, Integer id)  {
        userRepository.updateUserByName(id, updateUserDTO.name());
        userRepository.updateUserBySurname(id, updateUserDTO.surname());
        userRepository.updateUserByTelephone(id, updateUserDTO.telephone());
        var headers = new HttpHeaders();
        headers.add("Location", "http://techstageit.com/account/index.html");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }
}
