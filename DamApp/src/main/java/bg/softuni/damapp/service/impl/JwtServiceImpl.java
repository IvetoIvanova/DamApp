package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final String jwtKey;
    private final long expiration;

    public JwtServiceImpl(@Value("${jwt_key}") String jwtKey,
                          @Value("${jwt_expiration}") long expiration) {
        this.jwtKey = jwtKey;
        this.expiration = expiration;
    }

    @Override
    public String generateToken(String userId, Map<String, Object> claims) {
        var now = new Date();

        return Jwts
                .builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
