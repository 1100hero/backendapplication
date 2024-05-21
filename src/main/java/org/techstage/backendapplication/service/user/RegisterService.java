package org.techstage.backendapplication.service.user;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techstage.backendapplication.model.token.Token;
import org.techstage.backendapplication.model.user.Role;
import org.techstage.backendapplication.model.user.User;
import org.techstage.backendapplication.repository.UserRepository;
import org.techstage.backendapplication.service.token.JwtService;
import org.techstage.backendapplication.service.token.TokenService;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    //private final EmailSender emailSender;

    public RegisterService(PasswordEncoder passwordEncoder, UserRepository userRepository, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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

        var jwtService = new JwtService();
        var token = jwtService.generateToken(user);
        var confirmationToken = new Token(
                token,
                jwtService.extractClaim(token, Claims::getIssuedAt),
                jwtService.extractClaim(token, Claims::getExpiration),
                user
        );
        tokenService.saveConfirmationToken(confirmationToken);
        //emailSender.sendAccountConfirmation(request.getEmail(), token, request.getName());
        var link = "http://localhost:8080/registration/confirm?token=" + token;
        System.out.println(link);
    }

    @Transactional
    public HttpStatus confirmToken(String token) {
        var confirmationToken = tokenService
                .getToken(token);

        if (confirmationToken.isEmpty()) return HttpStatus.NOT_FOUND;

        if (confirmationToken.get().getConfirmedAt() != null) return HttpStatus.CONFLICT;

        if (confirmationToken.get().getExpiresAt().before(new Date())) return HttpStatus.GONE;

        tokenService.setConfirmedAt(token);
        var jwt = new JwtService();
        userRepository.updateConfirmedTokenById(
                userRepository.findIdByUsername(jwt.extractUsername(
                        confirmationToken.get().getToken())).get(),
                confirmationToken.get().getToken());

        userRepository.enableUser(confirmationToken.get().getUser().getEmail(), confirmationToken.get().getToken());

        return HttpStatus.OK;
    }


}
