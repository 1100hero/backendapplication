package org.techstage.backendapplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDTO userRegistrationDTO) {
        userService.save(userRegistrationDTO);
        return "redirect:/techstageit.com/index.html";
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
