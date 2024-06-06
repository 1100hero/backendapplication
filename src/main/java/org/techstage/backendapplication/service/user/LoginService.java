package org.techstage.backendapplication.service.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.TokenRepository;
import org.techstage.backendapplication.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(User request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalStateException("User not found."));
        if (tokenRepository.findTokenByUserEmailIfConfirmed(user.getEmail()).isEmpty())
            return "token_non_confermato";
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );
        return tokenRepository.findTokenByUserEmailIfConfirmed(user.getEmail()).get().getToken();
    }

    public ResponseEntity<String> checkPassword(User user) {
        if (userRepository.findOneByEmail(user.getEmail()).isEmpty())
            return ResponseEntity.ok("REFUSED");
        if(!userRepository.checkIfEnabled(user.getEmail()))
            return ResponseEntity.ok("NOT_VERIFIED");

        return passwordEncoder.matches(user.getPassword(),
                userRepository.findOneByEmail(user.getEmail()).get().getPassword()) ?
                ResponseEntity.ok("ACCEPTED") : ResponseEntity.ok("REFUSED");
    }
}
