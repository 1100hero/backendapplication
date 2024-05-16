package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.model.AuthenticationResponse;
import org.techstage.backendapplication.model.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.AuthenticationService;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationService authenticationService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> register(@ModelAttribute("user") User request) {
        var token = authenticationService.register(request);
        var headers = new HttpHeaders();
        headers.add("Location", "http://51.254.36.232/techstageit.com/index.html?registration=success");
        headers.add("Token", String.valueOf(token));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/registration/check/mail-{email}")
    public ResponseEntity<Boolean> isAlreadyRegisteredWithMail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userRepository.existsByEmail(email));
    }

    @GetMapping("/registration/check/number-{telephone}")
    public ResponseEntity<Boolean> isAlreadyRegisteredWithPhoneNumber(@PathVariable("telephone") String number) {
        return ResponseEntity.ok(userRepository.existsByTelephone(number));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.login(request));
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
