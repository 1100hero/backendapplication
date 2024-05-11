package org.techstage.backendapplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.UserService;
import org.techstage.backendapplication.web.dto.UserLoginDTO;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
@CrossOrigin("*")
public class UserLoginController {

    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> onLogin(@ModelAttribute("user") UserLoginDTO userLoginDTO) {
        var token = userService.login(userLoginDTO);
        var headers = new HttpHeaders();
        headers.add("Location", "http://51.254.36.232/techstageit.com/index.html?login=success");
        headers.add("Token", token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> isPasswordExact(@ModelAttribute("user") UserLoginDTO userLoginDTO) {
        var check = userRepository
                .findOneByEmailAndPassword(userLoginDTO.getEmail(),
                        userLoginDTO.getPassword()).isEmpty();
        return check ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }
}
