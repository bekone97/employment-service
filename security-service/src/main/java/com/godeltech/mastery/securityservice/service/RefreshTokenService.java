package com.godeltech.mastery.securityservice.service;

import com.godeltech.mastery.securityservice.model.entity.RefreshToken;
import com.godeltech.mastery.securityservice.model.entity.User;

import java.time.LocalDateTime;

public interface RefreshTokenService {
    RefreshToken getByRefreshToken(String token);
    RefreshToken save(RefreshToken token);
    RefreshToken replaceToken(RefreshToken token, User user, LocalDateTime currentDate);
    RefreshToken createRefreshToken(User user);
    void deactivateRefreshTokensByUserId(Long userId);
}
