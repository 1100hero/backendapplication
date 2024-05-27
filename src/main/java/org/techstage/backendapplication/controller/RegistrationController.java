package org.techstage.backendapplication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.user.RegisterService;

@RestController
@CrossOrigin("*")
public class RegistrationController {

    private final UserRepository userRepository;
    private final RegisterService registerService;

    public RegistrationController(UserRepository userRepository, RegisterService registerService) {
        this.userRepository = userRepository;
        this.registerService = registerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> register(@ModelAttribute("user") User request) {
        var headers = new HttpHeaders();
        if(registerService.register(request) == 1){
            headers.add("Location", "http://techstageit.com/index.html?registration=success");
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(headers).build();
    }

    @GetMapping("/registration/confirm")
    public ResponseEntity<Void> confirm(@RequestParam("token") String token) {
        var headers = new HttpHeaders();
        var status = registerService.confirmToken(token);
        if(status == HttpStatus.OK) {
            headers.add("Location", "http://techstageit.com/index.html?verification=success");
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        }
        return ResponseEntity.status(status).headers(headers).build();
    }

    @GetMapping("/registration/check/mail-{email}")
    public ResponseEntity<Boolean> isAlreadyRegisteredWithMail(@PathVariable("email") String email) {
        if (userRepository.existsByEmail(email) && userRepository.checkIfEnabled(email)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @GetMapping("/registration/check/number-{telephone}")
    public ResponseEntity<Boolean> isAlreadyRegisteredWithPhoneNumber(@PathVariable("telephone") String number) {
        return ResponseEntity.ok(userRepository.existsByTelephone(number));
    }
}
