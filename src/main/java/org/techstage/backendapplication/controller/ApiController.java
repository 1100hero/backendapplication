package org.techstage.backendapplication.controller;

import org.springframework.web.bind.annotation.*;
import org.techstage.backendapplication.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class ApiController {

    private final UserRepository userRepository;

    public ApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping( "/api/credentials")
    public Map<?, ?> getUserCredentials(@RequestParam("userName") String userName) {
        var user = userRepository.findUserByName(userName);
        if (user.isEmpty()) return null;
        if (!user.get().isEnabled()) return null;
        var map = new HashMap<>();
        map.put("Nome", user.get().getName());
        map.put("Cognome", user.get().getSurname());
        map.put("Email", user.get().getEmail());
        map.put("Telephone", user.get().getTelephone());
        return map;
    }
}
