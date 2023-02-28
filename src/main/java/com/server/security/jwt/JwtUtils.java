package com.server.security.jwt;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.server.models.User;
import com.server.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${config.jwtSecret}")
  private String jwtSecret;

  @Value("${config.jwtExpirationSeconds}")
  private int jwtExpirationSeconds;

  @Value("${config.jwtRefreshExpirationSeconds}")
  private int refreshTokenDurationSeconds;

  @Value("${config.jwtCookieName}")
  private String jwtCookie;

  @Value("${config.jwtRefreshCookieName}")
  private String jwtRefreshCookie;

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getEmail());
    return generateCookie(jwtCookie, jwt, "/api", jwtExpirationSeconds);
  }

  public ResponseCookie generateJwtCookie(User user) {
    String jwt = generateTokenFromUsername(user.getEmail());
    return generateCookie(jwtCookie, jwt, "/api", jwtExpirationSeconds);
  }

  public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
    return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth/refreshtoken", refreshTokenDurationSeconds);
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    return getCookieValueByName(request, jwtCookie);
  }

  public String getJwtRefreshFromCookies(HttpServletRequest request) {
    return getCookieValueByName(request, jwtRefreshCookie);
  }

  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  public ResponseCookie getCleanJwtRefreshCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path("/api/auth/refreshtoken").build();
    return cookie;
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    System.out.println("validate jwt token? " + authToken);

    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + (jwtExpirationSeconds * 1000)))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  private ResponseCookie generateCookie(String name, String value, String path, int maxAge) {
    ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(maxAge).httpOnly(true).build();
    return cookie;
  }

  private String getCookieValueByName(HttpServletRequest request, String name) {
    Cookie cookie = WebUtils.getCookie(request, name);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }
}
