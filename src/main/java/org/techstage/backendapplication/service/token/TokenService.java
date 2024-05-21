package org.techstage.backendapplication.service.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.token.Token;
import org.techstage.backendapplication.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveConfirmationToken(Token token) {
        tokenRepository.save(token);
    }

    public Optional<Token> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        tokenRepository
                .updateConfirmedAt(token, LocalDateTime.now());
    }
}
