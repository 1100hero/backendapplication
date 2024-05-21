package org.techstage.backendapplication.service.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.techstage.backendapplication.model.user.User;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "88c2cab1c8211cdd211268c53cca00e0743383ce37798e04c00b8cc0a4f0c8e3";

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        var username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // DA REVISIONARE
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        var claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String generateToken(User user) {
        var now = LocalDateTime.now();
        var zdtNow = now.atZone(ZoneId.systemDefault());
        var issueDate = Date.from(zdtNow.toInstant());

        var expirationTime = now.plusMinutes(15);
        var zdtExpiration = expirationTime.atZone(ZoneId.systemDefault());
        var expirationDate = Date.from(zdtExpiration.toInstant());
        return Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getSigninKey())
                .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
