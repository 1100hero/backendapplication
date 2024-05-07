package org.techstage.backendapplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.techstage.backendapplication.service.UserService;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class UserLoginController {

    private UserService userService;

    @PostMapping
    public String onLogin() {
        return "Ciao";
    }
}
