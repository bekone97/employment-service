package com.godeltech.mastery.securityservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godeltech.mastery.securityservice.model.entity.User;
import com.godeltech.mastery.securityservice.service.JWTService;
import com.godeltech.mastery.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SECURITY_USERNAME_KEY = "username";
    public static final String SECURITY_PASSWORD_KEY = "password";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";


    private final UserService userService;
    private final JWTService jwtService;



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter(SECURITY_USERNAME_KEY);
        String password = request.getParameter(SECURITY_PASSWORD_KEY);
        log.info("Username is :{} and password is :{}",username,password);
        return new UsernamePasswordAuthenticationToken(username,password);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username);
        Map<String, String> tokens = jwtService.createAccessAndRefreshTokens(user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }
}
