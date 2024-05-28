package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.model.token.TokenDTO;
import org.techstage.backendapplication.model.token.email.EmailSender;
import org.techstage.backendapplication.model.user.UpdateUserDTO;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.api.ApiService;
import org.techstage.backendapplication.service.token.EmailService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class ApiController {

    private final UserRepository userRepository;
    private final ApiService apiService;

    public ApiController(UserRepository userRepository, EmailSender emailSender, ApiService apiService) {
        this.userRepository = userRepository;
        this.apiService = apiService;
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
        var user = userRepository.findUserByName(updateUserDTO.name());
        var headers = new HttpHeaders();
        headers.add("Location", "http://techstageit.com/account/index.html");
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).headers(headers).build();
        return null;
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
        if(user.isEmpty())
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/api/resetPassword")
    public void resetPassword(@RequestBody TokenDTO tokenDTO) {
        apiService.sendResetPswRequest(tokenDTO);
    }
}
