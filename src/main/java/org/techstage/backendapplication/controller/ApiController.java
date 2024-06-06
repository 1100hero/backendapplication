package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.model.dto.EmailDTO;
import org.techstage.backendapplication.model.dto.ResetPswDTO;
import org.techstage.backendapplication.model.dto.TokenDTO;
import org.techstage.backendapplication.model.dto.UpdateUserDTO;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.api.ApiService;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ApiController {

    private final UserRepository userRepository;
    private final ApiService apiService;
    private final PasswordEncoder passwordEncoder;

    public ApiController(UserRepository userRepository, ApiService apiService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.apiService = apiService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/credentials")
    public Map<?, ?> getUserCredentials(@RequestBody TokenDTO token) {
        return apiService.getCredentialsByToken(token.token());
    }

    @PostMapping("/credentials/update")
    public ResponseEntity<Void> updateUserCredentials(@RequestBody UpdateUserDTO updateUserDTO) {
        return apiService.updateCredentials(updateUserDTO);
    }

    @GetMapping("/credentials/check-name")
    public ResponseEntity<Boolean> checkCredentialsName(@RequestParam("name") String name) {
        return apiService.checkCredential(name, userRepository::findUserByName);
    }

    @GetMapping("/credentials/check-surname")
    public ResponseEntity<Boolean> checkCredentialsSurname(@RequestParam("surname") String surname) {
        return apiService.checkCredential(surname, userRepository::findUserBySurname);
    }

    @GetMapping("/credentials/check-telephone")
    public ResponseEntity<Boolean> checkCredentialsTelephone(@RequestParam("telephone") String telephone) {
        return apiService.checkCredential(telephone, userRepository::findUserByTelephone);
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody TokenDTO token) {
        apiService.sendResetPswRequest(token);
    }

    @PostMapping("/resetForgottenPassword")
    public void resetForgottenPsw(@RequestBody EmailDTO dto) {
        apiService.sendResetPswForgotRequest(dto.email());
    }

    @PostMapping("/check-password")
    public ResponseEntity<Boolean> checkPass(@RequestBody ResetPswDTO dto) {
        return apiService.checkPassword(dto);
    }

    @GetMapping("/reset")
    public ResponseEntity<Void> reset(@RequestParam("token") String token) {
        var headers = new HttpHeaders();
        headers.add("Location", "http://techstageit.com/reset-password/index.html?token="+token);
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePass(@RequestBody ResetPswDTO dto) {
        var headers = new HttpHeaders();
        if(userRepository.findUserByConfirmedToken(dto.token()).isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).headers(headers).build();
        userRepository.updateUserPasswordByConfirmedToken(dto.token(), passwordEncoder.encode(dto.psw()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
