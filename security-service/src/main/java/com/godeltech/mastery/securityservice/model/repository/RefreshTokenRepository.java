package com.godeltech.mastery.securityservice.model.repository;

import com.godeltech.mastery.securityservice.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    List<RefreshToken> findByUserId(Long userId);
}
