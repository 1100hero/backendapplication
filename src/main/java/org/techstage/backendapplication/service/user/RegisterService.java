package org.techstage.backendapplication.service.user;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techstage.backendapplication.model.token.Token;
import org.techstage.backendapplication.model.token.email.EmailSender;
import org.techstage.backendapplication.model.user.Role;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.TokenRepository;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.token.EmailService;
import org.techstage.backendapplication.service.token.JwtService;
import org.techstage.backendapplication.service.token.TokenService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final TokenRepository tokenRepository;

    public RegisterService(PasswordEncoder passwordEncoder, UserRepository userRepository, TokenService tokenService, EmailSender emailSender, TokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
        this.tokenRepository = tokenRepository;
    }

    protected int register(User request) {
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

        if (userRepository.countUsersByEmail(user.getEmail()) > 5) return 0;
        userRepository.save(user);

        var jwtService = new JwtService();
        var token = jwtService.generateToken(user);
        var confirmationToken = new Token(
                token,
                jwtService.extractClaim(token, Claims::getIssuedAt),
                jwtService.extractClaim(token, Claims::getExpiration),
                user
        );
        tokenService.saveConfirmationToken(confirmationToken);
        var link = "http://51.254.36.232:8085/registration/confirm?token=" + token;
        emailSender.send(user.getEmail(),
                EmailService.buildEmailVerification(user.getName(), link),
                "Conferma la tua email");
        return 1;
    }

    @Transactional
    public HttpStatus preConfirmToken(String token) {
        var confirmationToken = tokenService.getToken(token);

        if (confirmationToken.isEmpty()) return HttpStatus.NOT_FOUND;

        var tokenEntity = confirmationToken.get();
        if (tokenEntity.getConfirmedAt() != null) return HttpStatus.CONFLICT;

        if (tokenEntity.getExpiresAt().before(new Date())) return HttpStatus.GONE;

        var userIdOpt = tokenRepository.findUserIdByToken(tokenEntity.getToken());
        if (userIdOpt.isEmpty()) return HttpStatus.NOT_FOUND;

        var user = userRepository.findUserById(userIdOpt.get()).orElseThrow();

        tokenService.setConfirmedAt(token);
        userRepository.updateConfirmedTokenById(user.getId(), tokenEntity.getToken());
        userRepository.enableUser(tokenEntity.getUser().getEmail(), tokenEntity.getToken());

        tokenRepository.deleteTokenExcept(user.getId());
        userRepository.deleteAllByEnabledFalse();

        return HttpStatus.OK;
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void cleanExpiredToken() {
        ZoneId zoneId = ZoneId.of("Europe/Rome");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        Date expiredAt = Date.from(now.toInstant());

        var userIds = tokenRepository.findUserIdsWithExpiredTokens(expiredAt);
        if (userIds.isEmpty()) return;

        for (Integer id : userIds.get()) {
            tokenRepository.deleteTokenByUserId(id);
            userRepository.deleteById(id);
        }
    }

    public ResponseEntity<Void> registerUser(User request) {
        var headers = new HttpHeaders();
        if (register(request) == 1) {
            headers.add("Location", "http://techstageit.com/index.html?registration=success");
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(headers).build();
    }
}
