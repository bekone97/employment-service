package com.godeltech.mastery.securityservice.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.godeltech.mastery.securityservice.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface JWTService {
    String getUsernameByTokenHeader(String header);
    Map<String,String> createAccessAndRefreshTokens(User user);
    Map<String,String> createTokensByRefreshTokenHeader(String header);

}
