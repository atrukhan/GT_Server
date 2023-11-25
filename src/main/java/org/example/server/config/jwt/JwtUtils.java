package org.example.server.config.jwt;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import org.example.server.services.security.UserDetailsImpl;
@Component
public class JwtUtils {

    @Value("${app.jwtAccessSecret}")
    private String jwtAccessSecret;

    @Value("${app.jwtAccessExpirationMs}")
    private int jwtAccessExpirationMs;

    @Value("${app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    public int getJwtRefreshExpirationMs(){
        return jwtAccessExpirationMs;
    }

    public String generateJwtAccessToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getEmail())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtAccessExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtAccessSecret).compact();
    }

    public String generateJwtRefreshToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getEmail())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret).compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtAccessSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public String getEmailFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtAccessSecret).parseClaimsJws(jwt).getBody().getSubject();
    }

}