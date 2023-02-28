package com.server.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.exception.TokenRefreshException;
import com.server.models.RefreshToken;
import com.server.repositories.RefreshTokenRepository;
import com.server.repositories.UserRepository;

@Service
public class RefreshTokenService {
  @Value("${config.jwtRefreshExpirationSeconds}")
  private int refreshTokenDurationSeconds;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(String userId) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationSeconds));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(String userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
