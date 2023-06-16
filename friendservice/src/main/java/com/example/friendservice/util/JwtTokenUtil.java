package com.example.friendservice.util;

import com.example.friendservice.entity.User;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000 * 7; // 1 week

    Logger logger = Logger.getLogger(JwtTokenUtil.class);
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user) {
        logger.info("[Secret token]" + SECRET_KEY);
        Date current = new Date();
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getUserId(), user.getEmail()))
                .setIssuer("NguyenTrongMinh")
                .setIssuedAt(current)
                .setExpiration(new Date(current.getTime()+ EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject().split(",")[0]);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return false;
    }
}
