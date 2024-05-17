package org.techstage.backendapplication.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.AuthenticationResponse;
import org.techstage.backendapplication.model.Role;
import org.techstage.backendapplication.model.Token;
import org.techstage.backendapplication.model.User;
import org.techstage.backendapplication.repository.TokenRepository;
import org.techstage.backendapplication.repository.UserRepository;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public void register(User request) {
        var user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setTelephone(request.getTelephone());
        user.setUsername(user.getName().toLowerCase()
                .replaceAll("\\s+", "").concat(".")
                .concat(user.getSurname().toLowerCase()
                        .replaceAll("\\s+", "")));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public AuthenticationResponse login(User request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );
        var token = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(token, user);
        return new AuthenticationResponse(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if (validTokens.isEmpty()) return;
        validTokens.forEach(token ->
                token.setLoggedOut(true));
        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String jwt, User user) {
        var token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void deleteLoggedOutTokens() {
        tokenRepository.deleteByLoggedOutTrue();
    }
}
