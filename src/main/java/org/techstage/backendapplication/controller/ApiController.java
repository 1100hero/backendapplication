package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.model.dto.EmailDTO;
import org.techstage.backendapplication.model.dto.ResetPswDTO;
import org.techstage.backendapplication.model.dto.TokenDTO;
import org.techstage.backendapplication.model.token.email.EmailSender;
import org.techstage.backendapplication.model.dto.UpdateUserDTO;
import org.techstage.backendapplication.repository.TokenRepository;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.api.ApiService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class ApiController {

    private final UserRepository userRepository;
    private final ApiService apiService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiController(UserRepository userRepository, EmailSender emailSender, ApiService apiService, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.apiService = apiService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/credentials")
    public Map<?, ?> getUserCredentials(@RequestBody TokenDTO token) {
        var user = userRepository.findUserByConfirmedToken(token.token());
        if (user.isEmpty()) return null;
        var map = new HashMap<>();
        map.put("Name", user.get().getName());
        map.put("Surname", user.get().getSurname());
        map.put("Email", user.get().getEmail());
        map.put("Telephone", user.get().getTelephone());
        return map;
    }

    @PostMapping("/api/credentials/update")
    public ResponseEntity<Void> updateUserCredentials(@RequestBody UpdateUserDTO updateUserDTO) {
        var id = userRepository.findUserIdByToken(updateUserDTO.token());
        var headers = new HttpHeaders();
        if (id.isEmpty()) {
            headers.add("Location", "http://techstageit.com/index.html?update=failed");
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).headers(headers).build();
        }
        headers.add("Location",
                "http://techstageit.com/index.html?update=success");
        apiService.update(updateUserDTO, id.get());
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping("/api/credentials/check-name")
    public ResponseEntity<Boolean> checkCredentialsName(@RequestParam("name") String name) {
        var user = userRepository.findUserByName(name);
        if (user.isEmpty())
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/credentials/check-surname")
    public ResponseEntity<Boolean> checkCredentialsSurname(@RequestParam("surname") String surname) {
        var user = userRepository.findUserBySurname(surname);
        if (user.isEmpty())
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/credentials/check-telephone")
    public ResponseEntity<Boolean> checkCredentialsTelephone(@RequestParam("telephone") String telephone) {
        var user = userRepository.findUserByTelephone(telephone);
        if (user.isEmpty())
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/api/resetPassword")
    public void resetPassword(@RequestBody TokenDTO token) {
        apiService.sendResetPswRequest(token);
    }

    @PostMapping("/api/resetForgottenPassword")
    public void resetForgottenPsw(@RequestParam("email") String email) {
        apiService.sendResetPswForgotRequest(email);
    }

    @GetMapping("/api/reset")
    public ResponseEntity<Void> reset(@RequestParam("email") String email) {
        var headers = new HttpHeaders();
        headers.add("Location", "http://techstageit.com/reset-password/index.html?email="+email);
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @PostMapping("/api/check-password")
    public ResponseEntity<Boolean> checkPass(@RequestBody ResetPswDTO dto) {
        var user = userRepository.findUserByEmail(dto.email());
        return user
                .filter(value -> !passwordEncoder
                        .matches(dto.psw(), value.getPassword()))
                .map(value -> ResponseEntity.ok(true))
                .orElseGet(() -> ResponseEntity.ok(false));
    }

    @PostMapping("/api/update-password")
    public ResponseEntity<Void> updatePass(@RequestBody ResetPswDTO dto) {
        var headers = new HttpHeaders();
        userRepository.updateUserPasswordByEmail(dto.email(), passwordEncoder.encode(dto.psw()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
