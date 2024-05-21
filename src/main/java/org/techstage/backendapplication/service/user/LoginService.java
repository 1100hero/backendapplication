package org.techstage.backendapplication.service.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.TokenRepository;
import org.techstage.backendapplication.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public LoginService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
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
}
