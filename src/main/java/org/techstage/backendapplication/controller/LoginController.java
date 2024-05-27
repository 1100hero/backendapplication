package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.user.LoginService;

@RestController
@CrossOrigin("*")
public class LoginController {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(LoginService loginService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User request) {
        var token = loginService.login(request);
        var headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); // Solitamente si usa l'header Authorization per i token JWT
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // Usa HttpStatus.FOUND per il redirect
    }

    @PostMapping("/login/check")
    public ResponseEntity<Boolean> isPasswordExact(@RequestBody User user) {
        if (userRepository.findOneByEmail(user.getEmail()).isEmpty())
            return ResponseEntity.ok(false);

        return passwordEncoder.matches(user.getPassword(),
                userRepository.findOneByEmail(user.getEmail()).get().getPassword()) ?
                ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }
}
