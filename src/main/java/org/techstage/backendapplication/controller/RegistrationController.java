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
        registerService.register(request);
        var headers = new HttpHeaders();
        headers.add("Location", "http://techstageit.com/index.html?registration=success");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping(value = "/registration/confirm")
    public ResponseEntity<HttpStatus> confirm(@RequestParam("token") String token) {
        return new ResponseEntity<>(registerService.confirmToken(token));
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
