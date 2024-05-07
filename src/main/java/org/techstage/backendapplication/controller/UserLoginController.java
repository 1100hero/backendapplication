package org.techstage.backendapplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.service.UserService;
import org.techstage.backendapplication.web.dto.UserLoginDTO;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
@CrossOrigin("*")
public class UserLoginController {

    private UserService userService;

    @PostMapping
    public void onLogin(@ModelAttribute("user") UserLoginDTO userLoginDTO) {

    }
}
