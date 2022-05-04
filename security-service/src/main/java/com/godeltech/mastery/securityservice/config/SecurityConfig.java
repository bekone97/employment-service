package com.godeltech.mastery.securityservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godeltech.mastery.securityservice.filter.JwtAuthenticationProvider;
import com.godeltech.mastery.securityservice.filter.JwtAuthenticationTokenFilter;
import com.godeltech.mastery.securityservice.filter.JwtLoginFilter;
import com.godeltech.mastery.securityservice.handling.JwtAuthenticationFailureHandler;
import com.godeltech.mastery.securityservice.handling.JwtAuthenticationSuccessHandler;
import com.godeltech.mastery.securityservice.service.JWTService;
import com.godeltech.mastery.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final JWTService jwtService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider()));
    }

    private JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userService);
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilterBean(){

        final JwtAuthenticationTokenFilter authenticationTokenFilter =
                new JwtAuthenticationTokenFilter(new AntPathRequestMatcher("/users"));
        authenticationTokenFilter.setAuthenticationManager(authenticationManager());
        authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        authenticationTokenFilter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler(objectMapper));

    return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtLoginFilter loginFilter = new JwtLoginFilter(userService,jwtService);
        loginFilter.setFilterProcessesUrl("/users/authenticate");
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests().antMatchers("/users/token/refresh").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(loginFilter)
                .addFilterBefore(jwtAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
