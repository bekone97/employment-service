package com.godeltech.mastery.securityservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.godeltech.mastery.securityservice.model.entity.RefreshToken;
import com.godeltech.mastery.securityservice.model.entity.User;
import com.godeltech.mastery.securityservice.service.JWTService;
import com.godeltech.mastery.securityservice.service.RefreshTokenService;
import com.godeltech.mastery.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.godeltech.mastery.securityservice.filter.JwtAuthenticationTokenFilter.TOKEN_PREFIX;
import static com.godeltech.mastery.securityservice.filter.JwtLoginFilter.ACCESS_TOKEN;
import static com.godeltech.mastery.securityservice.filter.JwtLoginFilter.REFRESH_TOKEN;
import static com.godeltech.mastery.securityservice.utils.ConstantUtil.Token.*;
import static java.lang.System.currentTimeMillis;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtServiceImpl implements JWTService {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Override
    public String getUsernameByTokenHeader(String header) {
        var token = getValidHeader(header);
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token).getSubject();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }

    @Override
    @Transactional
    public Map<String, String> createAccessAndRefreshTokens(User user) {
        refreshTokenService.deactivateRefreshTokensByUserId(user.getId());
        String accessToken = createAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();
        return createTokensMap(accessToken, refreshToken);

    }

    @Override
    @Transactional
    public Map<String, String> createTokensByRefreshTokenHeader(String header) {
        var refreshToken = getValidHeader(header);
        var token = refreshTokenService.getByRefreshToken(refreshToken);
        User user = userService.getById(token.getUserId());
        return createTokensByRefreshToken(user, token);
    }

    private String getValidHeader(String header) {
        if (header != null && header.startsWith(TOKEN_PREFIX))
            return header.substring(TOKEN_PREFIX.length());
        throw new RuntimeException("Not valid header");

    }

    private Map<String, String> createTokensMap(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);
        return tokens;
    }

    private String createAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(ISSUER)
                .withClaim(USER_ID, user.getId())
                .withClaim(ROLES, user.getRole().name())
                .sign(getAlgorithm());
    }

    private Map<String, String> createTokensByRefreshToken(User user, RefreshToken refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, createAccessToken(user));
        tokens.put(REFRESH_TOKEN, replacedRefreshToken(user, refreshToken).toString());
        return tokens;
    }

    private RefreshToken replacedRefreshToken(User user, RefreshToken token) {
        var currentDate = LocalDateTime.now();
        return refreshTokenService.replaceToken(token, user, currentDate);
    }
}
