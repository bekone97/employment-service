package com.godeltech.mastery.securityservice.service.impl;

import com.godeltech.mastery.securityservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.securityservice.exception.TokenExpiredException;
import com.godeltech.mastery.securityservice.exception.TokenNotActiveException;
import com.godeltech.mastery.securityservice.model.entity.RefreshToken;
import com.godeltech.mastery.securityservice.model.entity.User;
import com.godeltech.mastery.securityservice.model.repository.RefreshTokenRepository;
import com.godeltech.mastery.securityservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

import static java.sql.Timestamp.valueOf;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Override
    public RefreshToken getByRefreshToken(String token) {
        return refreshTokenRepository.findRefreshTokenByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(RefreshToken.class, "refreshToken", token));
    }

    @Override
    @Transactional
    public RefreshToken save(RefreshToken token) {
        return refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public RefreshToken replaceToken(RefreshToken oldToken, User user, LocalDateTime currentDate) {
        checkOldToken(oldToken, user, currentDate);
        oldToken.setActive(false);
        oldToken.setRevoked(Timestamp.valueOf(currentDate));
        save(oldToken);
        String newToken = getRefreshToken(user, currentDate);
        var refreshToken = RefreshToken.builder()
                .token(Base64.getEncoder().encodeToString(newToken.getBytes()))
                .userId(user.getId())
                .expires(valueOf(currentDate.plusDays(7)))
                .created(valueOf(currentDate))
                .isActive(true)
                .replacedByToken(oldToken.getToken())
                .build();
        return save(refreshToken);
    }

    private void checkOldToken(RefreshToken oldToken, User user, LocalDateTime currentDate) {
        checkTokenIsActive(oldToken, user.getId());
        checkTokenExpires(oldToken, currentDate, user.getId());
    }

    private String getRefreshToken(User user, LocalDateTime currentDate) {
        return new StringBuffer(user.getUsername())
                .append(user.getId())
                .append(user.getRole())
                .append(currentDate)
                .append(currentDate.plusDays(7))
                .append(SECRET_KEY)
                .toString();
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        var currentDate = LocalDateTime.now();
        String token = getRefreshToken(user, currentDate);
        var refreshToken = RefreshToken.builder()
                .token(Base64.getEncoder().encodeToString(token.getBytes()))
                .userId(user.getId())
                .expires(valueOf(currentDate.plusDays(7)))
                .created(valueOf(currentDate))
                .isActive(true)
                .build();
        return save(refreshToken);
    }

    @Override
    public void deactivateRefreshTokensByUserId(Long userId) {
        refreshTokenRepository.findByUserId(userId)
                .stream()
                .filter(RefreshToken::isActive)
                .peek(refreshToken -> {
                    refreshToken.setActive(false);
                    refreshToken.setRevoked(Timestamp.valueOf(LocalDateTime.now()));
                })
                .forEach(refreshTokenRepository::save);
    }
    private void checkTokenIsActive(RefreshToken refreshToken,Long userId){
        if (refreshToken.isActive())
            return;
        deactivateRefreshTokensByUserId(userId);
        throw new TokenNotActiveException();
    }
    private void checkTokenExpires(RefreshToken refreshToken, LocalDateTime currentDate,Long userId){
        if(currentDate.isBefore(refreshToken.getExpires().toLocalDateTime()))
            return;
        deactivateRefreshTokensByUserId(userId);
        throw new TokenExpiredException(RefreshToken.class,"expires",refreshToken.getExpires());
    }

}
