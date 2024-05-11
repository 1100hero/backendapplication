package org.techstage.backendapplication.controller;

import io.netty.handler.codec.http2.Http2Headers;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.UserService;
import org.techstage.backendapplication.web.dto.UserRegistrationDTO;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
@CrossOrigin("*")
public class UserRegistrationController {

    private UserService userService;

    @Autowired
    private UserRepository repository;

    @PostMapping
    public ResponseEntity<Void> registerUserAccount(@ModelAttribute("user") UserRegistrationDTO userRegistrationDTO) {
        userService.save(userRegistrationDTO);
        var headers = new HttpHeaders();
        headers.add("Location", "http://51.254.36.232/techstageit.com/index.html?registration=success");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/check/mail-{email}")
    public ResponseEntity<Boolean> isAlreadyRegisteredWithMail(@PathVariable("email") String email) {
        return ResponseEntity.ok(repository.existsByEmail(email));
    }

    @GetMapping("/check/number-{telephone}")
    public ResponseEntity<Boolean> isAlreadyRegisteredWithPhoneNumber(@PathVariable("telephone") String number) {
        return ResponseEntity.ok(repository.existsByTelephone(number));
    }
}
