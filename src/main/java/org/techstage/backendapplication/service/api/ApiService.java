package org.techstage.backendapplication.service.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.dto.ResetPswDTO;
import org.techstage.backendapplication.model.dto.TokenDTO;
import org.techstage.backendapplication.model.dto.UpdateUserDTO;
import org.techstage.backendapplication.model.token.email.EmailSender;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.token.EmailService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ApiService {

    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    public ApiService(UserRepository userRepository, EmailSender emailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendResetPswRequest(TokenDTO dto) {
        var user = userRepository.findUserByConfirmedToken(dto.token());
        if (user.isEmpty()) return;
        var token = user.get().getConfirmedToken();
        var link = "http://51.254.36.232:8085/api/reset?token=" + token;
        emailSender.send(
                user.get().getEmail(),
                EmailService.buildEmailResetPsw(user.get().getName(), link),
                "Ripristina la password.");
    }

    public void sendResetPswForgotRequest(String email) {
        var user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) return;
        if (!user.get().isEnabled()) return;
        var token = user.get().getConfirmedToken();
        var link = "http://51.254.36.232:8085/api/reset?token="+token;
        emailSender.send(
                user.get().getEmail(),
                EmailService.buildEmailResetPsw(user.get().getName(), link),
                "Ripristina la password."
        );
    }

    public Map<?, ?> getCredentialsByToken(String token) {
        var user = userRepository.findUserByConfirmedToken(token);
        if (user.isEmpty()) return null;
        var map = new HashMap<>();
        map.put("Name", user.get().getName());
        map.put("Surname", user.get().getSurname());
        map.put("Email", user.get().getEmail());
        map.put("Telephone", user.get().getTelephone());
        return map;
    }

    private void update(UpdateUserDTO updateUserDTO, Integer id) {
        userRepository.updateUserByName(id, updateUserDTO.name());
        userRepository.updateUserBySurname(id, updateUserDTO.surname());
        userRepository.updateUserByTelephone(id, updateUserDTO.telephone());
    }

    public ResponseEntity<Void> updateCredentials(UpdateUserDTO updateUserDTO) {
        var id = userRepository.findUserIdByToken(updateUserDTO.token());
        var headers = new HttpHeaders();
        if (id.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).headers(headers).build();
        update(updateUserDTO, id.get());
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    public ResponseEntity<Boolean> checkCredential(String value, Function<String, Optional<User>> finder) {
        var user = finder.apply(value);
        return ResponseEntity.ok(user.isPresent());
    }

    public ResponseEntity<Boolean> checkPassword(ResetPswDTO dto) {
        var user = userRepository.findUserByConfirmedToken(dto.token());
        return user
                .filter(value -> !passwordEncoder
                        .matches(dto.psw(), value.getPassword()))
                .map(value -> ResponseEntity.ok(true))
                .orElseGet(() -> ResponseEntity.ok(false));
    }
}
