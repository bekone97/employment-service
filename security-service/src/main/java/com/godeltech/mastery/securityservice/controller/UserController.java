package com.godeltech.mastery.securityservice.controller;

import com.godeltech.mastery.securityservice.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final JWTService jwtService;

    @PostMapping("/token/refresh")
    public Map<String,String> refreshToken(@RequestHeader(AUTHORIZATION) String authorizationHeader){
       return jwtService.createTokensByRefreshTokenHeader(authorizationHeader);

    }
}
