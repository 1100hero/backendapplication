package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.user.LoginService;

@RestController
@CrossOrigin("*")
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody User request) {
        var token = loginService.login(request);
        var headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/check")
    public ResponseEntity<String> isPasswordExact(@RequestBody User user) {
        return loginService.checkPassword(user);
    }
}
