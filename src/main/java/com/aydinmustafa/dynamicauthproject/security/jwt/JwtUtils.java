package com.aydinmustafa.dynamicauthproject.security.jwt;


import com.aydinmustafa.dynamicauthproject.security.services.UserDetailsImpl;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${mustafaaydin.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mustafaaydin.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        //get the username since principal refers to username in this context
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("Here generate jwt token function will generate a jwt ");
        System.out.println(Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact());
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        System.out.println("getUsernameFromToken Function will return ");
        System.out.println(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject());
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            //json web token signed will be parsed into DefaultJwtParser() which will have setSigninKey method to set secret
            //key and then authToken is the token we pass via http header and parseClaimsJws() will return Jwt<Header, Claims>
            // which includes types Header and Claims
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {} thrown in jwtUtils.java class", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {} thrown in jwtUtils.java class", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {} thrown in jwtUtils.java class", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {} thrown in jwtUtils.java class", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {} thrown in jwtUtils.java class", e.getMessage());
        }

        return false;
    }
}

